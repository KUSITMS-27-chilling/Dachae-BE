package chilling.encore.utils.domain;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Long getLectureIdx() {
        return lectureIdx;
    }

    public void setLectureIdx(Long lectureIdx) {
        this.lectureIdx = lectureIdx;
    }

    @Override
    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(int goalNum) {
        this.goalNum = goalNum;
    }

    @Override
    public String getLectureObjective() {
        return lectureObjective;
    }

    public void setLectureObjective(String lectureObjective) {
        this.lectureObjective = lectureObjective;
    }

    @Override
    public String getLectureContent() {
        return lectureContent;
    }

    public void setLectureContent(String lectureContent) {
        this.lectureContent = lectureContent;
    }

    @Override
    public String getLectureMethod() {
        return lectureMethod;
    }

    public void setLectureMethod(String lectureMethod) {
        this.lectureMethod = lectureMethod;
    }

    @Override
    public String getLectureRequired() {
        return lectureRequired;
    }

    public void setLectureRequired(String lectureRequired) {
        this.lectureRequired = lectureRequired;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public List<LectureMessage> getLectureMessages() {
        return lectureMessages;
    }

    public void setLectureMessages(List<LectureMessage> lectureMessages) {
        this.lectureMessages = lectureMessages;
    }
}
