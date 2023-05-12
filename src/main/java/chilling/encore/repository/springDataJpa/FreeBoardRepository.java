package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.FreeBoard;
import chilling.encore.repository.querydsl.FreeBoardDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardDslRepository {
}
