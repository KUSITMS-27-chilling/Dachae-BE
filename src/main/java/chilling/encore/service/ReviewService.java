package chilling.encore.service;

import chilling.encore.domain.Review;
import chilling.encore.dto.ReviewDto.PopularReview;
import chilling.encore.dto.ReviewDto.ReviewPage;
import chilling.encore.dto.ReviewDto.SelectReview;
import chilling.encore.repository.springDataJpa.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.sound.sampled.ReverbType;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final int REVIEW_PAGE_SIZE = 8;

    public ReviewPage getReviewPage(String region, Integer page) {
        if (page == null) {
            page = 1;
        }

        Page<Review> reviewPage = getFullReview(region, page - 1);
        List<SelectReview> reviews = getReviews(reviewPage);
        return ReviewPage.from(reviewPage.getTotalPages(), reviews);
    }

    private List<SelectReview> getReviews(Page<Review> reviewPage) {
        List<SelectReview> reviews = new ArrayList<>();
        for (int i = 0; i < reviewPage.getContent().size(); i++) {
            Review review = reviewPage.getContent().get(i);
            reviews.add(SelectReview.from(review));
        }
        return reviews;
    }

    private Page<Review> getFullReview(String region, Integer page) {
        String[] regions = region.split(",");
        Pageable pageable = PageRequest.of(page, REVIEW_PAGE_SIZE, Sort.by("createdAt").descending());
        log.info("regions = {}", regions[0]);
        Page<Review> reviewPage = reviewRepository.findRegionReviewPage(regions, pageable);
        return reviewPage;
    }

    public List<PopularReview> getPopularReview(String region) {
        String[] regions = region.split(",");
//        List<Review> Reviews = new ArrayList<>();
//        for (String r : regions) {
//            Reviews.add(reviewRepository.findAllByProgram_LearningCenter_RegionByHitDesc(r));
//        }
        List<Review> Reviews = reviewRepository.findRegionReview(regions);
        List<PopularReview> popularReviews = new ArrayList<>();
        for (Review review : Reviews) {
            popularReviews.add(PopularReview.from(review));
            if (popularReviews.size() == 3) {
                break;
            }
        }
        return popularReviews;
    }
}
