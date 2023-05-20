package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.AlaramDto.AlarmResponse;
import chilling.encore.dto.AlaramDto.NewAlarm;
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

    public AlarmResponse getAlarm() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Set<String> notificationIds = redisRepository.getNotificationIds(String.valueOf(user.getUserIdx()), 0, -1);
        List<String> notifications = redisRepository.getNotifications(String.valueOf(user.getUserIdx()), notificationIds);
        Iterator<String> notificationIdIterator = notificationIds.iterator();
        List<NewAlarm> newAlarms = getNewAlarms(notifications, notificationIdIterator);

        return AlarmResponse.from(newAlarms);
    }

    private List<NewAlarm> getNewAlarms(List<String> notifications, Iterator<String> notificationIdIterator) {
        List<NewAlarm> newAlarms = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            String[] splitDatas = notifications.get(i).split(":");
            String boardType = splitDatas[0];
            String title = splitDatas[2];
            String nickName = splitDatas[3];
            String content = splitDatas[4];
            /**
             * 알람 관련해서 진행 중
             * 태그(mention), 언제 알람이 발생했는지 필요(createdAt)
             */
            String mention = splitDatas[5];
            String createdAt = splitDatas[6];

            if (boardType.equals("Listen")) {
                newAlarms.add(NewAlarm.from(notificationIdIterator.next(), null, Long.parseLong(splitDatas[1]), null,
                        mention, title, nickName, content, createdAt));
                continue;
            } else if (boardType.equals("Review")) {
                newAlarms.add(NewAlarm.from(notificationIdIterator.next(), null, null, Long.parseLong(splitDatas[1]),
                        mention, title, nickName, content, createdAt));
                continue;
            }
            newAlarms.add(NewAlarm.from(notificationIdIterator.next(), Long.parseLong(splitDatas[1]), null, null,
                    mention, title, nickName, content, createdAt));
        }
        return newAlarms;
    }
}
