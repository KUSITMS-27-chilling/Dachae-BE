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

@Data
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
}
