package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Review;
import chilling.encore.repository.querydsl.ReviewDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewDslRepository {

    Page<Review> findAllByProgram_LearningCenter_Region(String region, Pageable pageable);
}
