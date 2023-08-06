package chilling.encore.utils.domain;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.TeacherInfo;
import chilling.encore.domain.User;

public class MockTeacherInfo {
    public TeacherInfo getTeacherInfo(User user) {
        return TeacherInfo.builder()
                .teacherInfoIdx(1L)
                .user(user)
                .introduce("test")
                .career("test1,test2,test3")
                .certificate("test1,test2,test3")
                .profile("test")
                .years(1)
                .build();
    }
}
