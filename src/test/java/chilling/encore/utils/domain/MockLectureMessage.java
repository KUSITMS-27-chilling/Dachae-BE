package chilling.encore.utils.domain;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.User;

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
