package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.LectureMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureMessageRepository extends JpaRepository<LectureMessage, Long> {
}
