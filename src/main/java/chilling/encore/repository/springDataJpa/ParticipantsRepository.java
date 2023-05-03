package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
}
