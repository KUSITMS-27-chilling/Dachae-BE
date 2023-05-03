package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ReviewComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComments, Long> {
}
