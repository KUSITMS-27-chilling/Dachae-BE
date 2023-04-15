package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private String name;
    private String gender;
    private int age;
    private String email;
    private String phoneNumber;

    private String isTeacher;

    private int status;
    // 회원 정상 / 휴면 / 탈퇴 상태

    @CreationTimestamp
    private Date createdAt;
    private Date loginAt;
    private Date quitAt;
}
