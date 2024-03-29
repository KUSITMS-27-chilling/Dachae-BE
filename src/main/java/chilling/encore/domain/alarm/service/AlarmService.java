package chilling.encore.domain.alarm.service;

import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.alarm.dto.AlaramDto.AlarmResponse;
import chilling.encore.domain.alarm.dto.AlaramDto.NewAlarm;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    private final RedisRepository redisRepository;
    private final SecurityUtils securityUtils;

    public AlarmResponse getAlarm() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Set<String> notificationIds = redisRepository.getNotificationIds(String.valueOf(user.getUserIdx()), 0, -1);
        List<String> notifications = redisRepository.getNotifications(String.valueOf(user.getUserIdx()), notificationIds);
        Iterator<String> notificationIdIterator = notificationIds.iterator();
        List<NewAlarm> newAlarms = getNewAlarms(notifications, notificationIdIterator);

        return AlarmResponse.from(newAlarms);
    }

    private List<NewAlarm> getNewAlarms(List<String> notifications, Iterator<String> notificationIdIterator) {
        List<NewAlarm> newAlarms = new ArrayList<>();
        for (int i = notifications.size() - 1; i >= 0; i--) {
            String[] splitData = notifications.get(i).split(":");
            String boardType = splitData[0];
            String boardIdx = splitData[1];
            String title = splitData[2];
            String nickName = splitData[3];
            String content = splitData[4];
            String mention = splitData[5];
            String createdAt = splitData[6];

            newAlarms.add(NewAlarm.from(notificationIdIterator.next(), boardType, boardIdx,
                    mention, title, nickName, content, createdAt));
        }
        return newAlarms;
    }
}
