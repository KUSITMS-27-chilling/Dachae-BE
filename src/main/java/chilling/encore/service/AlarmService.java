package chilling.encore.service;

import chilling.encore.domain.*;
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
            ReviewComments reviewComments = reviewAlarms.get(i).getReviewComments();
            Review review = reviewComments.getReview();
            String title = "[" + String.valueOf(review.getWeek()) + "주차] " + review.getProgram().getProgramName();
            String content = reviewComments.getContent();
            NewAlarm alarm = NewAlarm.from(
                    null,
                    reviewAlarms.get(i).getReviewAlarmIdx(),
                    title,
                    reviewAlarms.get(i).getUser().getNickName(),
                    content
            );
            newAlarms.add(alarm);
        }
        for (int i = 0; i < listenAlarms.size(); i++) {
            ListenComments listenComments = listenAlarms.get(i).getListenComments();
            ListenTogether listenTogether = listenComments.getListenTogether();
            String title = listenTogether.getTitle();
            String content = listenComments.getContent();
            NewAlarm alarm = NewAlarm.from(
                    listenAlarms.get(i).getListenAlarmIdx(),
                    null,
                    title,
                    listenAlarms.get(i).getUser().getNickName(),
                    content
            );
            newAlarms.add(alarm);
        }
        return newAlarms;
    }
}
