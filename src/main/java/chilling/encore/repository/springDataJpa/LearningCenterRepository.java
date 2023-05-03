package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.LearningCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningCenterRepository extends JpaRepository<LearningCenter, Long> {
}
