package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.Program;
import chilling.encore.domain.Review;
import chilling.encore.domain.User;
import chilling.encore.dto.ReviewDto;
import chilling.encore.dto.ReviewDto.PopularReview;
import chilling.encore.dto.ReviewDto.ReviewPage;
import chilling.encore.dto.ReviewDto.SelectReview;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.ProgramRepository;
import chilling.encore.repository.springDataJpa.ReviewRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CenterRepository centerRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

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

    public ReviewDto.PopularReviewPage getPopularReview() {
        List<String> regions = new ArrayList<>();
        try {
            List<Review> userRegionsReviewList = login(regions);

            List<PopularReview> popularReviewList = new ArrayList<>();
            for (Review review : userRegionsReviewList) {
                popularReviewList.add(PopularReview.from(review));
            }

            return ReviewDto.PopularReviewPage.from(popularReviewList);
        } catch (ClassCastException e) {
            List<Review> userRegionsReviewList = notLogin(regions);

            List<PopularReview> popularReviewList = new ArrayList<>();
            for (Review review : userRegionsReviewList) {
                popularReviewList.add(PopularReview.from(review));
            }

            return ReviewDto.PopularReviewPage.from(popularReviewList);
        }
    }

    private List<Review> login(List<String> regions) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        regions.add(user.getRegion());
        if (user.getFavRegion() != null) {
            String[] favRegions = user.getFavRegion().split(",");
            for (int i = 0; i < favRegions.length; i++) {
                regions.add(favRegions[i]);
            }
        }
        return reviewRepository.findRegionReview(regions);
    }

    private List<Review> notLogin(List<String> regions) {
        List<Center> centers = centerRepository.findTop4ByOrderByFavCountDesc();
        for (int i = 0; i < centers.size(); i++) {
            regions.add(centers.get(i).getRegion());
        }
        return reviewRepository.findRegionReview(regions);
    }

    public void save(ReviewDto.CreateReviewRequest createReviewRequest) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Program program = programRepository.findById(createReviewRequest.getProgramIdx()).orElseThrow();

        Review review = Review.builder()
                .title(createReviewRequest.getTitle())
                .week(createReviewRequest.getWeek())
                .content(createReviewRequest.getContent())
                .image(createReviewRequest.getImage())
                .hit(0)
                .program(program)
                .user(securityUser).build();
        reviewRepository.save(review);

        User user = userRepository.findById(securityUser.getUserIdx()).orElseThrow();
        user.updateGrade();
    }
}
