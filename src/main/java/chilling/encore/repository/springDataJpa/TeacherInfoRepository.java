package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Long> {
}
