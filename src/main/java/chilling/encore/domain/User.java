package chilling.encore.domain;

import chilling.encore.dto.responseMessage.UserConstants;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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
    private String providerId;
    //해당에서 사용하는 id

    @CreationTimestamp
    private LocalDate createdAt;
    private LocalDate loginAt;

    public void updateLoginAt(LocalDate now) {
        this.loginAt = now;
    }
}
