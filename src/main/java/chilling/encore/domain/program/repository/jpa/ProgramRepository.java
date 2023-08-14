package chilling.encore.domain.program.repository.jpa;

import chilling.encore.domain.program.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<List<Program>> findTop3ByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(LocalDate now1, LocalDate now2, String region);
    Optional<List<Program>> findTop10ByStartDateBetweenAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(LocalDate thisMonth, LocalDate nextMonth, LocalDate now, String region);
    Optional<List<Program>> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(LocalDate now1, LocalDate now2, String region);
    Optional<Page<Program>> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(LocalDate now1, LocalDate now2, String region, Pageable pageable);
}