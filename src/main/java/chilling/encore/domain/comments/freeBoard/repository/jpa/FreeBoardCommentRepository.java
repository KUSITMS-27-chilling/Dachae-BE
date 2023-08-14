package chilling.encore.domain.comments.freeBoard.repository.jpa;

import chilling.encore.domain.comments.freeBoard.entity.FreeBoardComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComments, Long> {
    List<FreeBoardComments> findAllByFreeBoard_FreeBoardIdxOrderByCreatedAtAsc(Long freeBoardIdx);
}
