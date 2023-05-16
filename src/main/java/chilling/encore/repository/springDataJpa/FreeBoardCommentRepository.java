package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.FreeBoardComments;
import chilling.encore.domain.ReviewComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComments, Long> {
    List<FreeBoardComments> findAllByFreeBoard_FreeBoardIdxOrderByCreatedAtAsc(Long freeBoardIdx);
}
