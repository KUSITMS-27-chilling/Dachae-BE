package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterRepository extends JpaRepository<Center, Long> {
    List<Center> findTop4ByOrderByFavCountDesc();
    Center findByRegion(String region);
}
