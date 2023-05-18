package chilling.encore.dto;

import chilling.encore.domain.FreeBoardComments;
import chilling.encore.domain.ListenComments;
import chilling.encore.domain.ReviewComments;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class CommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글 생성을 위한 요청 객체")
    public static class CreateCommentsRequest {
        private final String content;
        private final Long parentIdx;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @ApiModel(description = "수강후기 댓글 조회를 위한 응답 객체")
    public static class ReviewCommentResponse {
        private final int cnt;
        private final List<SelectReviewComment> reviewCommentList;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @ApiModel(description = "같이들어요 댓글조회를 위한 응답 객체")
    public static class ListenCommentResponse {
        private final int cnt;
        private final List<SelectListenComment> listenCommentList;

    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @ApiModel(description = "자유게시판 댓글조회를 위한 응답 객체")
    public static class FreeCommentResponse {
        private final int cnt;
        private final List<SelectFreeComment> freeCommentList;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SelectReviewComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final boolean isDelete;
        private final String createAt;
        private final Long parentIdx;
        private final List<ChildReviewComment> childs;
        public static SelectReviewComment from(ReviewComments reviewComments, List<ChildReviewComment> childs) {
            User user = reviewComments.getUser();
            return SelectReviewComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .isDelete(reviewComments.isDelete())
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
    public static class SelectListenComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final boolean isDelete;
        private final String createAt;
        private final Long parentIdx;
        private final List<ChildListenComment> childs;
        public static SelectListenComment from(ListenComments listenComments, List<ChildListenComment> childs) {
            User user = listenComments.getUser();
            return SelectListenComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(listenComments.getContent())
                    .isDelete(listenComments.isDelete())
                    .createAt(listenComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .parentIdx(listenComments.getListenCommentIdx())
                    .childs(childs)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SelectFreeComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final boolean isDelete;
        private final String createAt;
        private final Long parentIdx;
        private final List<ChildFreeComment> childs;
        public static SelectFreeComment from(FreeBoardComments freeBoardComments, List<ChildFreeComment> childs) {
            User user = freeBoardComments.getUser();
            return SelectFreeComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(freeBoardComments.getContent())
                    .isDelete(freeBoardComments.isDelete())
                    .createAt(freeBoardComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .parentIdx(freeBoardComments.getFreeBoardCommentIdx())
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
        private final boolean isDelete;
        private final Long parentIdx;
        private final String createAt;

        public static ChildReviewComment from(ReviewComments reviewComments) {
            User user = reviewComments.getUser();
            return ChildReviewComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(reviewComments.getContent())
                    .isDelete(reviewComments.isDelete())
                    .parentIdx(reviewComments.getParent().getReviewCommentIdx())
                    .createAt(reviewComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ChildListenComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final boolean isDelete;
        private final Long parentIdx;
        private final String createAt;

        public static ChildListenComment from(ListenComments listenComments) {
            User user = listenComments.getUser();
            return ChildListenComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(listenComments.getContent())
                    .isDelete(listenComments.isDelete())
                    .parentIdx(listenComments.getParent().getListenCommentIdx())
                    .createAt(listenComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ChildFreeComment {
        private final String profile;
        private final String nickName;
        private final String content;
        private final boolean isDelete;
        private final Long parentIdx;
        private final String createAt;

        public static ChildFreeComment from(FreeBoardComments freeBoardComments) {
            User user = freeBoardComments.getUser();
            return ChildFreeComment.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .content(freeBoardComments.getContent())
                    .isDelete(freeBoardComments.isDelete())
                    .parentIdx(freeBoardComments.getParent().getFreeBoardCommentIdx())
                    .createAt(freeBoardComments.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .build();
        }
    }
}
