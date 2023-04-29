package chilling.encore.repository;

import chilling.encore.domain.ListenAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListenAlarmRepository extends JpaRepository<ListenAlarm, Long> {
}
