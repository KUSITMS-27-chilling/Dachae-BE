package chilling.encore.dto;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.TeacherInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class LectureDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class LectureBasicInfo {
        private final int price;
        private final int goalNum;
        private final List<String> lectureObjective;
        private final List<String> lectureContent;
        private final List<String> lectureMethod;
        private final List<String> lectureRequired;

        public static LectureBasicInfo from(Lecture lecture, List<String>[] proceeds) {
            return LectureBasicInfo.builder()
                    .price(lecture.getPrice())
                    .goalNum(lecture.getGoalNum())
                    .lectureObjective(proceeds[0])
                    .lectureContent(proceeds[1])
                    .lectureMethod(proceeds[2])
                    .lectureRequired(proceeds[3])
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class LectureImages {
        private final List<String> images;
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class LectureDetailsTeacher {
        private final String profile;
        private final List<String> careers;
        private final List<String> certificates;

        public static LectureDetailsTeacher from(String profile, List<String> careers, List<String> certificates) {
            return LectureDetailsTeacher.builder()
                    .profile(profile)
                    .careers(careers)
                    .certificates(certificates)
                    .build();
        }
    }
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
