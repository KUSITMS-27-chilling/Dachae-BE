package chilling.encore.dto;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.Review;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class ReviewDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ReviewPage {
        private final int totalReviewPage;
        private final List<SelectReview> reviews;
        public static ReviewPage from(int totalReviewPage, List<SelectReview> reviews) {
            return ReviewPage.builder()
                    .totalReviewPage(totalReviewPage)
                    .reviews(reviews)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SelectReview {
        private final Long reviewIdx;
        private final String profile;
        private final String nickName;
        private final String createdAt;
        private final String title;
        private final List<String> favFields;
        private final int week;
        private final List<String> image;
        private final String programName;
        private final List<String> tags;
        public static SelectReview from(
                Review review
        ) {
            User user = review.getUser();
            Program program = review.getProgram();
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null) {
                favField = List.of(user.getFavField().split(","));
            }
            List<String> image = new ArrayList<>();
            if (review.getImage() != null) {
                image = List.of(review.getImage().split(","));
            }
            return SelectReview.builder()
                    .reviewIdx(review.getReviewIdx())
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .createdAt(review.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .title(review.getTitle())
                    .favFields(favField)
                    .week(review.getWeek())
                    .image(image)
                    .programName(program.getProgramName())
                    .tags(List.of(program.getLearningCenter().getRegion(), program.getLearningCenter().getLearningName()))
                    .build();
        }
    }
}
