package chilling.encore.domain.teacherInfo.repository.jpa;

import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Long> {
}
