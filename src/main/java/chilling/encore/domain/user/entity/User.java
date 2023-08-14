package chilling.encore.domain.user.entity;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import chilling.encore.domain.user.constant.UserConstants;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String profile;
    @Column(nullable = false)
    private int status;
    // 회원 정상 / 휴면 / 탈퇴 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserConstants.Role role;
    private String provider;
    //google, facebook등등
    @CreationTimestamp
    private LocalDate createdAt;
    private LocalDate loginAt;
    @Column(nullable = false)
    private String region;
    private String favRegion;
    @Column(nullable = false)
    private int grade;
    private String favField;

    @OneToMany(mappedBy = "user")
    private List<ListenTogether> listenTogethers = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Participants> participants = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<FreeBoard> freeBoards = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<LectureMessage> lectureMessages = new ArrayList<>();
    @OneToOne(mappedBy = "user")
    private TeacherInfo teacherInfo = new TeacherInfo();

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
