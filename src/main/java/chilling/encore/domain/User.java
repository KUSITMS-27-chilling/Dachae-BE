package chilling.encore.domain;

import chilling.encore.dto.responseMessage.UserConstants;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    private String userId;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String password;
    private String nickName;
    private String phoneNumber;
    private String isTeacher;
    private int status;
    // 회원 정상 / 휴면 / 탈퇴 상태
    @Enumerated(EnumType.STRING)
    private UserConstants.Role role;
    private String provider;
    //google, facebook등등
    @CreationTimestamp
    private LocalDate createdAt;
    private LocalDate loginAt;
    private String region;
    private String favRegion;
    private int grade;
    private String favField;

    public void updateLoginAt(LocalDate now) {
        this.loginAt = now;
    }
}
