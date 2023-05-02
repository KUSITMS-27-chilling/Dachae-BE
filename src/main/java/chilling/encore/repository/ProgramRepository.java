package chilling.encore.repository;

import chilling.encore.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findTop3ByStartDateBeforeAndEndDateAfterAndLearningCenter_RegionOrderByStartDateDesc(LocalDate now1, LocalDate now2, String region);
    List<Program> findAllByStartDateAfterAndLearningCenter_Region(LocalDate yesterday, String region);
}
