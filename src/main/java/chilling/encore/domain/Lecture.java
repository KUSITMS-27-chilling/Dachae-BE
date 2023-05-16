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
    private String proceed;
    private String image;
    private String region;
    @OneToMany(mappedBy = "lecture")
    private List<LectureMessage> lectureMessages = new ArrayList<>();
}
