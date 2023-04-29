package chilling.encore.domain;

import lombok.*;

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
    private User user;
    @ManyToOne
    private ListenComments listenComments;

    private boolean isRead;
    private LocalDateTime createdAt;
}
