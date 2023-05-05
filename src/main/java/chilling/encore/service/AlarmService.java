package chilling.encore.service;

import chilling.encore.domain.ListenAlarm;
import chilling.encore.domain.ReviewAlarm;
import chilling.encore.domain.User;
import chilling.encore.dto.AlaramDto;
import chilling.encore.dto.AlaramDto.AlarmResponse;
import chilling.encore.dto.AlaramDto.NewAlarm;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenAlarmRepository;
import chilling.encore.repository.springDataJpa.ReviewAlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final ReviewAlarmRepository reviewAlarmRepository;
    private final ListenAlarmRepository listenAlarmRepository;

    public AlarmResponse getAlarm() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        List<ReviewAlarm> reviewAlarms = reviewAlarmRepository.findAllByIsReadFalseAndUser_UserIdx(user.getUserIdx());
        List<ListenAlarm> listenAlarms = listenAlarmRepository.findAllByIsReadFalseAndUser_UserIdx(user.getUserIdx());
        List<NewAlarm> newAlarms = getNewAlarms(reviewAlarms, listenAlarms);
        return AlarmResponse.from(newAlarms);
    }

    private List<NewAlarm> getNewAlarms(List<ReviewAlarm> reviewAlarms, List<ListenAlarm> listenAlarms) {
        List<NewAlarm> newAlarms = new ArrayList<>();

        for (int i = 0; i < reviewAlarms.size(); i++) {
            NewAlarm alarm = NewAlarm.from(
                    null,
                    reviewAlarms.get(i).getReviewAlarmIdx(),
                    reviewAlarms.get(i).getUser().getNickName()
            );
            newAlarms.add(alarm);
        }
        for (int i = 0; i < listenAlarms.size(); i++) {
            NewAlarm alarm = NewAlarm.from(
                    listenAlarms.get(i).getListenAlarmIdx(),
                    null,
                    listenAlarms.get(i).getUser().getNickName()
            );
            newAlarms.add(alarm);
        }
        return newAlarms;
    }
}
