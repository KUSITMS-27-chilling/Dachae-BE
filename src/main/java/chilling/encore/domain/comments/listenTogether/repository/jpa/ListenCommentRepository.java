package chilling.encore.domain.comments.listenTogether.repository.jpa;

import chilling.encore.domain.comments.listenTogether.entity.ListenComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenCommentRepository extends JpaRepository<ListenComments, Long> {
    List<ListenComments> findAllByListenTogether_ListenIdxOrderByCreatedAtAsc(Long listenIdx);

}
