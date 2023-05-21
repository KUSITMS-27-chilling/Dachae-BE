package chilling.encore.dto;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.User;
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
