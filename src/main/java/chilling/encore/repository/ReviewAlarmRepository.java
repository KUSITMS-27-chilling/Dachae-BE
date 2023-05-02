package chilling.encore.repository;

import chilling.encore.domain.ReviewAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewAlarmRepository extends JpaRepository<ReviewAlarm, Long> {
    List<ReviewAlarm> findAllByIsReadFalseAndUser_UserIdx(Long userIdx);
}
