package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    List<Participants> findAllByListenTogether_ListenIdx(Long listenTogetherIdx);
}
