package chilling.encore.domain.comments.review.repository.jpa;

import chilling.encore.domain.comments.review.entity.ReviewComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComments, Long> {
    List<ReviewComments> findAllByReview_ReviewIdxOrderByCreatedAtAsc(Long reviewIdx);
}
