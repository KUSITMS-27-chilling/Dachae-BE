package chilling.encore.domain.freeBoard.repository.jpa;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import chilling.encore.domain.freeBoard.repository.querydsl.FreeBoardDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardDslRepository {
}
