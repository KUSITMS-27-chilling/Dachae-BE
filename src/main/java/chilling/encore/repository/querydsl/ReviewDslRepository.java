package chilling.encore.repository.querydsl;

import chilling.encore.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewDslRepository {

    Page<Review> findRegionReview(String[] region, Pageable pageable);
}
