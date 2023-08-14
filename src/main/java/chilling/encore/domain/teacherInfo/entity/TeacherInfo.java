package chilling.encore.domain.teacherInfo.entity;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TeacherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherInfoIdx;
    @OneToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
    private String introduce; //한줄 소개
    @Column(columnDefinition = "TEXT")
    private String career; //경력
    private String certificate; //자격증
    private String profile;
    private int years;
    @OneToMany(mappedBy = "teacherInfo")
    private List<Lecture> lectures = new ArrayList<>();
}
