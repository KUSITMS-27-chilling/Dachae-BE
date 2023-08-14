package chilling.encore.domain.lecture.dto;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class LectureMessageDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class CreatedLectureMessage {
        private final String tel;
        private final String email;
        private final String content;

        public static LectureMessage to(Lecture lecture, User user, CreatedLectureMessage createdLectureMessage) {
            return LectureMessage.builder()
                    .lecture(lecture)
                    .user(user)
                    .content(createdLectureMessage.getContent())
                    .tel(createdLectureMessage.getTel())
                    .email(createdLectureMessage.getEmail())
                    .build();
        }
    }
}
