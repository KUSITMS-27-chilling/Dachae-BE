package chilling.encore.utils.domain;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.TeacherInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MockLecture extends Lecture{
    public MockLecture(Long lectureIdx, TeacherInfo teacherInfo, String region) {
        this.lectureIdx = lectureIdx;
        this.teacherInfo = teacherInfo;
        this.region = region;
    }
    private Long lectureIdx;
    private TeacherInfo teacherInfo;
    private String title = "test";
    private String category = "test";
    private int price = 10;
    private int goalNum = 10;
    private String lectureObjective = "test1,test2,test3";
    private String lectureContent = "test1,test2,test3";
    private String lectureMethod = "test1,test2,test3";
    private String lectureRequired = "test1,test2,test3";
    private String image = "test1,test2,test3";
    private String region;
    private LocalDate createdAt = LocalDate.now();
    private List<LectureMessage> lectureMessages = new ArrayList<>();

}
