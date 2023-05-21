package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @ColumnDefault("false")
    private boolean isRead;
    @CreationTimestamp
    private LocalDate createdAt;
}
