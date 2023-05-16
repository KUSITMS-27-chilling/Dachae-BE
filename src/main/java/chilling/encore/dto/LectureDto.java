package chilling.encore.dto;

import chilling.encore.domain.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class LectureDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class LectureInfo {
        private final Long lectureIdx;
        private final String title;
        private final String category;
        private final String profile;
        private final String name;
        private final int years;
        private final String introduce;

        public static LectureInfo from(Lecture lecture) {
            return LectureInfo.builder()
                    .lectureIdx(lecture.getLectureIdx())
                    .title(lecture.getTitle())
                    .category(lecture.getCategory())
                    .profile(lecture.getTeacherInfo().getProfile())
                    .name(lecture.getTeacherInfo().getUser().getName())
                    .years(lecture.getTeacherInfo().getYears())
                    .introduce(lecture.getTeacherInfo().getIntroduce())
                    .build();
        }
    }
}
