package chilling.encore.domain.lecture.entity;

import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
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
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherInfoIdx", nullable = false)
    private TeacherInfo teacherInfo;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int goalNum;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String lectureObjective;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String lectureContent;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String lectureMethod;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String lectureRequired;
    @Column(columnDefinition = "TEXT")
    private String image;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
    @OneToMany(mappedBy = "lecture")
    private List<LectureMessage> lectureMessages = new ArrayList<>();
}
