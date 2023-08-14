package chilling.encore.domain.review.repository.jpa;

import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.review.repository.querydsl.ReviewDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewDslRepository {

    Page<Review> findAllByProgram_LearningCenter_Region(String region, Pageable pageable);

    Review findByReviewIdx(Long reviewIdx);

    List<Review> findTop2ByUser_UserIdxOrderByUpdatedAtDesc(Long userIdx);

//    List<ReviewDto.PopularReview> findAllByOrderByHitDesc();

//    List<Review> findAllByOrderByHitDesc();
}
