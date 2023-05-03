package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ListenTogether;
import chilling.encore.repository.querydsl.ListenTogetherDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ListenTogetherRepository extends JpaRepository<ListenTogether, Long>, ListenTogetherDslRepository {
//    ListenTogether findTopByOrderByHitsDesc(String region);
    List<ListenTogether> findTopByOrderByHitDesc();

//    ListenTogether findAllByRegion(String region);
//    List<ListenTogether> findAllByRegion();

//    ListenTogether findByRegion(String region);

    Page<ListenTogether> findAllByProgram_LearningCenter_Region(String region, Pageable pageable);
}
