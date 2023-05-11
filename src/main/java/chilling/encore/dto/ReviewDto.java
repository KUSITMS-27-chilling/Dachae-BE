package chilling.encore.dto;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.Review;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
    @ApiModel(description = "수강후기 페이징 조회 응답 객체")
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
        private final String content;
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
                    .content(review.getContent())
                    .favFields(favField)
                    .week(review.getWeek())
                    .image(image)
                    .programName(program.getProgramName())
                    .tags(List.of(program.getLearningCenter().getRegion(), program.getLearningCenter().getLearningName()))
                    .build();
        }
    }

    @Getter
    @Builder
    @ApiModel(description = "수강후기 인기글 조회 응답 객체")
    public static class PopularReviewPage {
        private List<PopularReview> popularReviews;

        public static PopularReviewPage from(List<PopularReview> popularReviews) {
            return PopularReviewPage.builder()
                    .popularReviews(popularReviews)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PopularReview {
        private Long reviewIdx;
        private String title;

        public static PopularReview from(Review review) {
            return PopularReview.builder()
                    .reviewIdx(review.getReviewIdx())
                    .title(review.getTitle())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @ApiModel(description = "수강후기 작성을 위한 요청 객체")
    public static class CreateReviewRequest {
        private String title;
        private String region;
        private Long programIdx;
        private String programName;
        private int week;
        private String content;
        private String image;
    }


    @Getter
    @Builder
    @ApiOperation(value = "나의 수강후기 조회를 위한 응답 객체")
    public static class SelectMyReview {
        private Long reviewIdx;
        private String title;
        private LocalDateTime updatedAt;

        public static SelectMyReview from(Review review) {
            return SelectMyReview.builder()
                    .reviewIdx(review.getReviewIdx())
                    .title(review.getTitle())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }
}
