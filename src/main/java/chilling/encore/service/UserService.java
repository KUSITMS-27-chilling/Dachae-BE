package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.User;
import chilling.encore.dto.AlaramDto;
import chilling.encore.dto.AlaramDto.AlarmResponse;
import chilling.encore.dto.UserDto.UserLoginRequest;
import chilling.encore.dto.UserDto.UserLoginResponse;
import chilling.encore.dto.UserDto.UserSignUpRequest;
import chilling.encore.exception.CenterException;
import chilling.encore.exception.CenterException.NoSuchRegionException;
import chilling.encore.exception.UserException;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static chilling.encore.dto.UserDto.*;
import static chilling.encore.dto.responseMessage.UserConstants.Role.ROLE_USER;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisRepository redisRepository;

    //회원가입
    public User signUp(UserSignUpRequest userSignUpRequest) {
        Center center = centerRepository.findByRegion(userSignUpRequest.getRegion()).orElseThrow(() -> new NoSuchRegionException());
        center.plusFavCount();

        User user = User.builder()
                .userId(userSignUpRequest.getId())
                .name(userSignUpRequest.getName())
                .gender(userSignUpRequest.getGender())
                .age(userSignUpRequest.getAge())
                .email(userSignUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignUpRequest.getPassword()))
                .nickName(userSignUpRequest.getNickName())
                .phoneNumber(userSignUpRequest.getPhoneNumber())
                .role(ROLE_USER)
                .status(0)
                .region(userSignUpRequest.getRegion())
                .profile(userSignUpRequest.getProfile())
                .build();
        return userRepository.save(user);
    }

    //Oauth2 회원가입
    public User oauth2signUp(Oauth2SignUpRequest oauth2SignUpRequest) {
        Center center = centerRepository.findByRegion(oauth2SignUpRequest.getRegion()).orElseThrow(() -> new NoSuchRegionException());
        center.plusFavCount();

        String id = oauth2SignUpRequest.getProvider() + oauth2SignUpRequest.getId();
        User user = User.builder()
                .userId(id)
                .name(oauth2SignUpRequest.getName())
                .gender(oauth2SignUpRequest.getGender())
                .age(oauth2SignUpRequest.getAge())
                .email(oauth2SignUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickName(oauth2SignUpRequest.getProvider() + "_" + oauth2SignUpRequest.getId())
                .phoneNumber(oauth2SignUpRequest.getPhoneNumber())
                .role(ROLE_USER)
                .status(0)
                .provider(oauth2SignUpRequest.getProvider())
                .region(oauth2SignUpRequest.getRegion())
                .profile(oauth2SignUpRequest.getProfile())
                .build();
        return userRepository.save(user);
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        try {
            TokenInfoResponse tokenInfoResponse = validateLogin(userLoginRequest);

            String id = userLoginRequest.getId();
            User user = userRepository.findByUserId(id).orElseThrow(() -> new UserException.NoSuchRegionException());
            LocalDate now = LocalDate.now();
            user.updateLoginAt(now);

            return UserLoginResponse.from(tokenInfoResponse);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    private TokenInfoResponse validateLogin(UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.getId(), userLoginRequest.getPassword());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfoResponse tokenInfoResponse = jwtTokenProvider.createToken(authentication);
        return tokenInfoResponse;
    }

    public boolean checkIdDup(String id) {
        Optional<User> byUserId = userRepository.findByUserId(id);
        if (byUserId.isPresent())
            return false;
        return true;
    }

    public boolean checkNick(String nickName) {
        Optional<User> byNickName = userRepository.findByNickName(nickName);
        if (byNickName.isPresent())
            return false;
        return true;
    }

    public User validateUserId(String id) {
        User user = userRepository.findByUserId(id).orElseThrow(() -> new UserException.NoSuchRegionException());
        return user;
    }

    public UserGrade getGrade() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        UserGrade grade = UserGrade.from(user);
        return grade;
    }

    public UserFavRegion getFavCenter() {
        List<String> centers = new ArrayList<>();
        try {
            return getLoginRegion(centers);
        } catch (ClassCastException e) {
            return notLoginRegion(centers);
        }
    }

    private UserFavRegion getLoginRegion(List<String> centers) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        String region = user.getRegion();
        centers.add(region);
        if (user.getFavRegion() != null) {
            String[] split = user.getFavRegion().split(",");
            for (int i = 0; i < split.length; i++)
                centers.add(split[i]);
        }
        return UserFavRegion.from(centers);
    }

    private UserFavRegion notLoginRegion(List<String> centers) {
        List<Center> topCenters = centerRepository.findTop4ByOrderByFavCountDesc();
        for (int i = 0; i < topCenters.size(); i++)
            centers.add(topCenters.get(i).getRegion());
        return UserFavRegion.from(centers);
    }

    public UserFavRegion onlyFavRegion() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        List<String> favRegion = new ArrayList<>();
        if (user.getFavRegion() != null) {
            favRegion = List.of(user.getFavRegion().split(","));
        }
        return UserFavRegion.from(favRegion);
    }

    public void editFavRegion(EditFavRegion favRegion) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateFavRegion(favRegion.getFavRegion());
    }

    public void editNickName(EditNickName editNickName) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateNickName(editNickName.getNickName());
    }

    public void editFavField(EditFavField editFavField) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateFavField(editFavField.getFavField());
    }

    public UserInfo getUserInfo() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        return UserInfo.from(user);
    }

    public GetTotalParticipants getUserParticipant() {
        User user = userRepository.findById(
                SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        int participantsTotal = user.getParticipants().size();
        return GetTotalParticipants.from(participantsTotal);
    }

    public GetTotalWrite getTotalWrite() {
        User user = userRepository.findById(
                SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        int listenTotal = user.getListenTogethers().size();
        int reviewTotal = user.getReviews().size();
        int freeTotal = user.getFreeBoards().size();
        int allTotal = listenTotal + reviewTotal;

        return GetTotalWrite.from(allTotal, listenTotal, reviewTotal, freeTotal);
    }

    /**
     * 배움 소식 결정 되면 다시 진행
     */

    public AlarmResponse getAlarm() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Set<String> notificationIds = redisRepository.getNotificationIds(String.valueOf(user.getUserIdx()), 0, -1);
        List<String> notifications = redisRepository.getNotifications(String.valueOf(user.getUserIdx()), notificationIds);
        Iterator<String> notificationIdIterator = notificationIds.iterator();
        List<AlaramDto.NewAlarm> newAlarms = getNewAlarms(notifications, notificationIdIterator);

        return AlarmResponse.from(newAlarms);
    }

    private List<AlaramDto.NewAlarm> getNewAlarms(List<String> notifications, Iterator<String> notificationIdIterator) {
//        List<AlaramDto.NewAlarm> newAlarms = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            String[] splitDatas = notifications.get(i).split(":");
//            String boardType = splitDatas[0];
//            String title = splitDatas[2];
//            String nickName = splitDatas[3];
//            String content = splitDatas[4];
//            if (boardType.equals("Listen")) {
//                newAlarms.add(AlaramDto.NewAlarm.from(notificationIdIterator.next(), Long.parseLong(splitDatas[1]),
//                        null, title, nickName, content));
//                continue;
//            }
//            newAlarms.add(AlaramDto.NewAlarm.from(notificationIdIterator.next(), null, Long.parseLong(splitDatas[1]),
//                    title, nickName, content));
//        }
        return null;
    }
}
