package chilling.encore.domain.listenTogether.entity;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantsIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listenIdx", nullable = false)
    private ListenTogether listenTogether;
}
