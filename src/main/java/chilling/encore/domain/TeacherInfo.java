package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class TeacherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherInfoIdx;
    @OneToOne
    @JoinColumn(name = "userIdx")
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
