package chilling.encore.domain.freeBoard.repository.querydsl;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardDslRepository {
    Page<FreeBoard> findRegionFreeBoard(String region, String orderBy, Pageable pageable);
    List<String> findPopularFreeBoard(List<String> regions);
}
