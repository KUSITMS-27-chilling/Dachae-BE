package chilling.encore.domain.user.service;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.user.dto.UserDto;
import chilling.encore.domain.center.exception.CenterException;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static chilling.encore.domain.user.constant.UserConstants.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSingUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CenterRepository centerRepository;

    public User signUp(UserDto.UserSignUpRequest userSignUpRequest) {
        Center center = centerRepository.findByRegion(userSignUpRequest.getRegion()).orElseThrow(() -> new CenterException.NoSuchRegionException());
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
    public User oauth2signUp(UserDto.Oauth2SignUpRequest oauth2SignUpRequest) {
        Center center = centerRepository.findByRegion(oauth2SignUpRequest.getRegion()).orElseThrow(() -> new CenterException.NoSuchRegionException());
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
}
