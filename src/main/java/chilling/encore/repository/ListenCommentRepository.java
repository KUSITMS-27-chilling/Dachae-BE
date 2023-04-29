package chilling.encore.repository;

import chilling.encore.domain.ListenComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListenCommentRepository extends JpaRepository<ListenComments, Long> {
}
