package chilling.encore.dto;

import chilling.encore.domain.ListenComments;
import chilling.encore.domain.ReviewComments;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public abstract class CommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateCommentsRequest {
        private final int ref;
        private final int refOrder;
        private final int step;
        private final Long parentIdx;
        private final String content;
        private final int childSum;
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
        private final Long parentIdx;
        private final LocalDateTime createAt;
        public static ReviewCommentResponse from(ReviewComments reviewComments) {
            User user = reviewComments.getUser();
            return ReviewCommentResponse.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .step(reviewComments.getStep())
                    .parentIdx(reviewComments.getParentIdx())
                    .createAt(reviewComments.getCreatedAt())
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
