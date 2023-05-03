package chilling.encore.repository;

import chilling.encore.domain.ListenTogether;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenTogetherRepository extends JpaRepository<ListenTogether, Long> {
//    ListenTogether findTopByOrderByHitsDesc(String region);
    List<ListenTogether> findTopByOrderByHitDesc();

//    ListenTogether findAllByRegion(String region);
//    List<ListenTogether> findAllByRegion();

//    ListenTogether findByRegion(String region);
}
