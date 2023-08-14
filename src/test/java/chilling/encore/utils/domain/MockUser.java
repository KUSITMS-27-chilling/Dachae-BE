package chilling.encore.utils.domain;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.user.constant.UserConstants;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static chilling.encore.domain.user.constant.UserConstants.Role.ROLE_USER;

public class MockUser extends User {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private Long userIdx = 10000L;
    private String userId = "forTest";
    private String name = "forTest";
    private String gender = "male";
    private int age = 10;
    private String email = "forTest";
    private String password = "forTest";
    private String nickName = "forTest";
    private String phoneNumber = "forTest";
    private String profile = "forTest";
    private int status = 1;
    // 회원 정상 / 휴면 / 탈퇴 상태
    private UserConstants.Role role = ROLE_USER;
    private String provider = "forTest";
    private LocalDate createdAt = LocalDate.now();
    private LocalDate loginAt = LocalDate.now();
    private String region = "강서구";
    private String favRegion = "강남구,동작구";
    private int grade = 1;
    private String favField = "축구, 컴퓨터";
    private List<ListenTogether> listenTogethers = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Participants> participants = new ArrayList<>();
    private List<FreeBoard> freeBoards = new ArrayList<>();
    private List<LectureMessage> lectureMessages = new ArrayList<>();
    private TeacherInfo teacherInfo;

    public void updateLoginAt(LocalDate now) {
        this.loginAt = now;
    }
    public void updateFavRegion(String favRegion) {
        this.favRegion = favRegion;
    }

    public void updateFavField(String favField) {
        this.favField = favField;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateGrade() {
        this.grade += 2;
    }

    public void setUserIdx(Long userIdx) {
        this.userIdx = userIdx;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRole(UserConstants.Role role) {
        this.role = role;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setLoginAt(LocalDate loginAt) {
        this.loginAt = loginAt;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setFavRegion(String favRegion) {
        this.favRegion = favRegion;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setFavField(String favField) {
        this.favField = favField;
    }

    public void setListenTogethers(List<ListenTogether> listenTogethers) {
        this.listenTogethers = listenTogethers;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setParticipants(List<Participants> participants) {
        this.participants = participants;
    }

    public void setFreeBoards(List<FreeBoard> freeBoards) {
        this.freeBoards = freeBoards;
    }

    public void setLectureMessages(List<LectureMessage> lectureMessages) {
        this.lectureMessages = lectureMessages;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    @Override
    public Long getUserIdx() {
        return userIdx;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getProfile() {
        return profile;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public UserConstants.Role getRole() {
        return role;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDate getLoginAt() {
        return loginAt;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getFavRegion() {
        return favRegion;
    }

    @Override
    public int getGrade() {
        return grade;
    }

    @Override
    public String getFavField() {
        return favField;
    }

    @Override
    public List<ListenTogether> getListenTogethers() {
        return listenTogethers;
    }

    @Override
    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public List<Participants> getParticipants() {
        return participants;
    }

    @Override
    public List<FreeBoard> getFreeBoards() {
        return freeBoards;
    }

    @Override
    public List<LectureMessage> getLectureMessages() {
        return lectureMessages;
    }

    @Override
    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }
}
