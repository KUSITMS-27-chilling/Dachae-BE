package chilling.encore.domain.review.service;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.review.dto.ReviewDto;
import chilling.encore.domain.review.dto.ReviewDto.PopularReview;
import chilling.encore.domain.review.dto.ReviewDto.ReviewPage;
import chilling.encore.domain.review.dto.ReviewDto.SelectReview;
import chilling.encore.domain.program.exception.ProgramException;
import chilling.encore.domain.review.exception.ReviewException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.program.repository.jpa.ProgramRepository;
import chilling.encore.domain.review.repository.jpa.ReviewRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
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
    private final SecurityUtils securityUtils;

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

    public SelectReview getReview(Long reviewIdx) {
        Review review = reviewRepository.findById(reviewIdx).orElseThrow(() -> new NoSuchIdxException());
        return SelectReview.from(review);
    }

    public List<ReviewDto.SelectMyReview> getMyReview() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        List<Review> myReviews = reviewRepository.findTop2ByUser_UserIdxOrderByUpdatedAtDesc(user.getUserIdx());
        List<ReviewDto.SelectMyReview> myReviewSelect = new ArrayList<>();
        for (Review myReview : myReviews) {
            myReviewSelect.add(ReviewDto.SelectMyReview.from(myReview));
        }
        return myReviewSelect;
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
            List<Review> favRegionsReviewList = notLogin(regions);

            List<PopularReview> popularReviewList = new ArrayList<>();
            for (Review review : favRegionsReviewList) {
                popularReviewList.add(PopularReview.from(review));
            }

            return ReviewDto.PopularReviewPage.from(popularReviewList);
        }
    }

    private List<Review> login(List<String> regions) {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
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
            log.info("regions = {}", centers.get(i).getRegion());
        }
        return reviewRepository.findRegionReview(regions);
    }

    public void save(ReviewDto.CreateReviewRequest createReviewRequest) {
        User user = userRepository.findById(securityUtils.getLoggedInUser()
                .orElseThrow(() -> new ClassCastException("NotLogin"))
                .getUserIdx()).get();
        Program program = programRepository.findById(createReviewRequest.getProgramIdx()).orElseThrow(() -> new ProgramException.NoSuchIdxException());

        Review review = Review.builder()
                .title(createReviewRequest.getTitle())
                .week(createReviewRequest.getWeek())
                .content(createReviewRequest.getContent())
                .image(createReviewRequest.getImage())
                .hit(0)
                .program(program)
                .user(user).build();
        reviewRepository.save(review);
        user.updateGrade();
    }

}
