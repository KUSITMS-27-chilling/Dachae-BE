package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ListenTogether;
import chilling.encore.repository.querydsl.ListenTogetherDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenTogetherRepository extends JpaRepository<ListenTogether, Long>, ListenTogetherDslRepository {
//    ListenTogether findTopByOrderByHitsDesc(String region);
    List<ListenTogether> findTopByOrderByHitDesc();

//    ListenTogether findAllByRegion(String region);
//    List<ListenTogether> findAllByRegion();

//    ListenTogether findByRegion(String region);
}
