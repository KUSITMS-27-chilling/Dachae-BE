package chilling.encore.repository.querydsl;

import chilling.encore.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewDslRepository {

    Page<Review> findRegionReviewPage(String[] region, Pageable pageable);

    List<Review> findRegionReview(String[] region);
}
