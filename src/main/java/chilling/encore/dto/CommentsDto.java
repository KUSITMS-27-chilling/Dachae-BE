package chilling.encore.dto;

import chilling.encore.domain.ListenComments;
import chilling.encore.domain.Review;
import chilling.encore.domain.ReviewComments;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class CommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateReviewCommentsRequest {
        private final int ref;
        private final int refOrder;
        private final int step;
        private final String content;
        private final int childSum;
        private final Long parentIdx;
    }
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateListenCommentsRequest {
        private final int ref;
        private final int refOrder;
        private final int step;
        private final String content;
        private final int childSum;
//        private final ReviewComments parent;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @ApiModel(description = "수강후기 댓글조회를 위한 응답 객체")
    public static class ReviewCommentResponse {
        private final String profile;
        private final String nickName;
        private final String content;
//        int ref;
//        int refOrder;
        private final int step;
        private final List<ReviewComments> child;
        private final LocalDateTime createAt;
        public static ReviewCommentResponse from(ReviewComments reviewComments) {
            User user = reviewComments.getUser();
            return ReviewCommentResponse.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .step(reviewComments.getStep())
                    .createAt(reviewComments.getCreatedAt())
                    .child(reviewComments.getChild())
                    .build();
        }
    }


    @Getter
    @Builder
    @RequiredArgsConstructor
    @ApiModel(description = "같이들어요 댓글조회를 위한 응답 객체")
    public static class ListenCommentResponse {
        private final String profile;
        private final String nickName;
        private final String content;
//        int ref;
//        int refOrder;
        private final int step;
        private final Long parentIdx;
        private final LocalDateTime createAt;

        public static ListenCommentResponse from(ListenComments listenComments) {
            User user = listenComments.getUser();
            return ListenCommentResponse.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(listenComments.getContent())
                    .step(listenComments.getStep())
                    .parentIdx(listenComments.getParentIdx())
                    .createAt(listenComments.getCreatedAt())
                    .build();
        }
    }
}
