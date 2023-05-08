package chilling.encore.repository.querydsl;

import chilling.encore.domain.ListenTogether;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ListenTogetherDslRepository {

    Page<ListenTogether> findRegionListenTogether(String[] region, Pageable pageable);
    List<ListenTogether> findPopularListenTogether(List<String> regions);
}
