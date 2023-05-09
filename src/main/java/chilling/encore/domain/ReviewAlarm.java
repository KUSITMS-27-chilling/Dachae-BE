package chilling.encore.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ReviewAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewAlarmIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewCommentIdx")
    private ReviewComments reviewComments;

    private boolean isRead;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
