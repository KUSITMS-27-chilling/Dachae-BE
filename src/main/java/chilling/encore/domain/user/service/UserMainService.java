package chilling.encore.domain.user.service;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.user.exception.UserException.NoSuchIdxException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static chilling.encore.domain.user.dto.UserDto.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserMainService {
    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final RedisRepository redisRepository;
    private final SecurityUtils securityUtils;
    private final String USER_PLUS = "LearningInfo";

    public User validateUserId(String id) {
        User user = userRepository.findByUserId(id).orElseThrow(() -> new NoSuchIdxException());
        return user;
    }

    public UserGrade getGrade() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
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

    public UserRegion getRegion() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        String region = user.getRegion();
        return UserRegion.builder()
                .region(region).build();
    }

    private UserFavRegion getLoginRegion(List<String> centers) {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
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
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        List<String> favRegion = new ArrayList<>();
        if (user.getFavRegion() != null) {
            favRegion = List.of(user.getFavRegion().split(","));
        }
        return UserFavRegion.from(favRegion);
    }

    public List<LearningInfo> getLearningInfo() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Set<String> learningInfoIds = redisRepository.getLearningInfoIds(user.getUserIdx() + USER_PLUS, 0, -1);
        List<String> learningInfos = redisRepository.getLearningInfos(user.getUserIdx() + USER_PLUS, learningInfoIds);
        Iterator<String> learningInfoIdIterator = learningInfoIds.iterator();

        return getLearningInfos(learningInfoIds, learningInfos, learningInfoIdIterator);
    }

    @NotNull
    private List<LearningInfo> getLearningInfos(Set<String> learningInfoIds, List<String> learningInfos, Iterator<String> learningInfoIdIterator) {
        List<LearningInfo> learningInfosResult = new ArrayList<>();
        for (int i = learningInfoIds.size() - 1; i >= 0; i--) {
            String[] splitData = learningInfos.get(i).split(":");
            String isFin = splitData[0];
            String listenIdx = splitData[1];
            String title = splitData[2];
            String nickName = splitData[3];

            LearningInfo learningInfo = LearningInfo.from(learningInfoIdIterator.next(), isFin, listenIdx, title, nickName);
            learningInfosResult.add(learningInfo);
        }
        return learningInfosResult;
    }
}
