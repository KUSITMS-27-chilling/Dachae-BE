package chilling.encore.domain.lecture.repository.jpa;

import chilling.encore.domain.lecture.entity.LectureMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureMessageRepository extends JpaRepository<LectureMessage, Long> {
}
