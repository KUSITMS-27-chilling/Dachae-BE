package chilling.encore.utils.domain;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import chilling.encore.domain.user.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MockTeacherInfo extends TeacherInfo{
    public MockTeacherInfo(Long idx, User user) {
        this.teacherInfoIdx = idx;
        this.user = user;
    }
    private Long teacherInfoIdx;
    private User user;
    private String introduce = "test"; //한줄 소개
    private String career = "test1,test2,test3"; //경력
    private String certificate = "test1,test2,test3"; //자격증
    private String profile = "test";
    private int years = 1;
    private List<Lecture> lectures = new ArrayList<>();
}
