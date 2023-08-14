package chilling.encore.domain.center.repository.jpa;

import chilling.encore.domain.center.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    List<Center> findTop4ByOrderByFavCountDesc();
    Optional<Center> findByRegion(String region);
}
