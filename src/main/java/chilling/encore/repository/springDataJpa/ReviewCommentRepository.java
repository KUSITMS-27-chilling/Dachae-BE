package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ReviewComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComments, Long> {
    List<ReviewComments> findAllByReview_ReviewIdxOrderByCreatedAtAsc(Long reviewIdx);
}
