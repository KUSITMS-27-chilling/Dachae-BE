package chilling.encore.utils.domain;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import chilling.encore.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Long getTeacherInfoIdx() {
        return teacherInfoIdx;
    }

    public void setTeacherInfoIdx(Long teacherInfoIdx) {
        this.teacherInfoIdx = teacherInfoIdx;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    @Override
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    @Override
    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
