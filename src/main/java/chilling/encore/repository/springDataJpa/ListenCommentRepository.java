package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ListenComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenCommentRepository extends JpaRepository<ListenComments, Long> {
    List<ListenComments> findAllByListenTogether_ListenIdxOrderByCreatedAtAsc(Long listenIdx);

}
