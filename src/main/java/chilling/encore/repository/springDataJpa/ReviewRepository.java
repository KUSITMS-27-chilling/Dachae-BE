package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
