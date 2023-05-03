package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findTop3ByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(LocalDate now1, LocalDate now2, String region);
    List<Program> findAllByStartDateGreaterThanEqualAndLearningCenter_Region(LocalDate yesterday, String region);
    Page<Program> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(LocalDate now1, LocalDate now2, String region, Pageable pageable);
}
