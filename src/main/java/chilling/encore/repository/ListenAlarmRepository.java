package chilling.encore.repository;

import chilling.encore.domain.ListenAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenAlarmRepository extends JpaRepository<ListenAlarm, Long> {

    List<ListenAlarm> findAllByIsReadFalseAndUser_UserIdx(Long userIdx);
}
