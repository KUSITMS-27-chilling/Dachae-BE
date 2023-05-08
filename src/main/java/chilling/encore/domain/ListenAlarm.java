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
public class ListenAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listenAlarmIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private User user;
    @ManyToOne
    @JoinColumn(name = "listenCommentIdx")
    private ListenComments listenComments;

    private boolean isRead;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
