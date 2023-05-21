package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureIdx;
    @ManyToOne
    @JoinColumn(name = "teacherInfoIdx")
    private TeacherInfo teacherInfo;
    private String title;
    private String category;
    private int price;
    private int goalNum;
    @Column(columnDefinition = "TEXT")
    private String lectureObjective;
    @Column(columnDefinition = "TEXT")
    private String lectureContent;
    @Column(columnDefinition = "TEXT")
    private String lectureMethod;
    @Column(columnDefinition = "TEXT")
    private String lectureRequired;
    @Column(columnDefinition = "TEXT")
    private String image;
    private String region;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "lecture")
    private List<LectureMessage> lectureMessages = new ArrayList<>();
}
