package chilling.encore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LectureMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureMessageIdx;
    @ManyToOne
    @JoinColumn(name = "lectureIdx")
    private Lecture lecture;
    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;
    @Column(columnDefinition = "text")
    private String content;
    private String email;
    private String tel;
}
