package chilling.encore.utils.domain;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.TeacherInfo;
import chilling.encore.domain.User;

import java.time.LocalDate;

public class MockLecture {
    public Lecture getLecture(Long idx, String region, TeacherInfo teacherInfo, User user) {
        return Lecture.builder()
                .lectureIdx(idx)
                .teacherInfo(teacherInfo)
                .title("test")
                .category("test")
                .price(10)
                .goalNum(1)
                .lectureObjective("test1,test2,test3")
                .lectureContent("test1,test2,test3")
                .lectureMethod("test1,test2,test3")
                .lectureRequired("test1,test2,test3")
                .image("test1,test2,test3")
                .region(region)
                .createdAt(LocalDate.now())
                .build();
    }
}
