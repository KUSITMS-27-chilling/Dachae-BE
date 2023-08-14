package chilling.encore.domain.listenTogether.repository.jpa;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    Boolean existsByUserAndListenTogether(User user, ListenTogether listenTogether);
}
