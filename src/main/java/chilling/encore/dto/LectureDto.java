package chilling.encore.dto;

import chilling.encore.domain.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class LectureDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class LecturePage {
        private final int totalPage;
        private final List<LectureInfo> lectureInfos;

        public static LecturePage from(int totalPage, List<LectureInfo> lectureInfos) {
            return LecturePage.builder()
                    .totalPage(totalPage)
                    .lectureInfos(lectureInfos)
                    .build();
        }
    }
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
