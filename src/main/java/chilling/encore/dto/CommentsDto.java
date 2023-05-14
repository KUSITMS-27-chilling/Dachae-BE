package chilling.encore.dto;

import chilling.encore.domain.ListenComments;
import chilling.encore.domain.ReviewComments;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class CommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateReviewCommentsRequest {
        private final String content;
        private final Long parentIdx;
    }
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateListenCommentsRequest {
        private final int ref;
        private final int refOrder;
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
        private final String createAt;
        private final Long parentIdx;
        private final List<ChildReviewComment> childs;
        public static ReviewCommentResponse from(ReviewComments reviewComments, List<ChildReviewComment> childs) {
            User user = reviewComments.getUser();
            return ReviewCommentResponse.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .createAt(reviewComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .parentIdx(reviewComments.getReviewCommentIdx())
                    .childs(childs)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ChildReviewComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final Long parentIdx;
        private final String createAt;
        public static ChildReviewComment from(ReviewComments reviewComments) {
            User user = reviewComments.getUser();
            return ChildReviewComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .parentIdx(reviewComments.getParent().getReviewCommentIdx())
                    .createAt(reviewComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
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
        private final Long parentIdx;
        private final LocalDateTime createAt;

        public static ListenCommentResponse from(ListenComments listenComments) {
            User user = listenComments.getUser();
            return ListenCommentResponse.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(listenComments.getContent())
                    .parentIdx(listenComments.getParentIdx())
                    .createAt(listenComments.getCreatedAt())
                    .build();
        }
    }
}
