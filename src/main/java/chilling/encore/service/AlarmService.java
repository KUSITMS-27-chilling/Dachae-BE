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
            if (boardType.equals("Listen")) {
                newAlarms.add(NewAlarm.from(notificationIdIterator.next(), Long.parseLong(splitDatas[1]),
                        null, title, nickName, content));
                continue;
            }
            newAlarms.add(NewAlarm.from(notificationIdIterator.next(), null, Long.parseLong(splitDatas[1]),
                    title, nickName, content));
        }
        return newAlarms;
    }
}
