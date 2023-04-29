package chilling.encore.domain;

import lombok.*;

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
    private Long reviewAlarm;

    @ManyToOne
    private User user;
    @ManyToOne
    private ReviewComments reviewComments;

    private boolean isRead;
    private LocalDateTime createdAt;
}
