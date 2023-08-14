package chilling.encore.domain.lecture.entity;

import chilling.encore.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LectureMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureMessageIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectureIdx", nullable = false)
    private Lecture lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
    @Column(columnDefinition = "text", nullable = false)
    private String content;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String tel;
    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isRead;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate createdAt;
}
