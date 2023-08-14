package chilling.encore.utils.domain;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.user.entity.User;

import java.time.LocalDate;

public class MockLectureMessage {
    public LectureMessage getMockLectureMessage(Long idx, Lecture lecture, User user) {
        return LectureMessage.builder()
                .lectureMessageIdx(idx)
                .lecture(lecture)
                .user(user)
                .content("test")
                .email("test")
                .tel("test")
                .isRead(false)
                .createdAt(LocalDate.now())
                .build();
    }
}
