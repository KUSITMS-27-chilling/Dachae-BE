package chilling.encore.repository;

import chilling.encore.domain.ReviewAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAlarmRepository extends JpaRepository<ReviewAlarm, Long> {
}
