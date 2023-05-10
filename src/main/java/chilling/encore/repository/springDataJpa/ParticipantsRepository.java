package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Participants;
import chilling.encore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    Boolean existsByUserAndListenTogether(User user, ListenTogether listenTogether);
}
