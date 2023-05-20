package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto.*;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final ListenCommentRepository listenCommentRepository;
    private final ListenTogetherRepository listenTogetherRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;

    private int cnt;

    public void reviewCommentSave(Long reviewIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findById(reviewIdx).orElseThrow();
        /**
         * 게시글의 주인과 댓글 작성자가 같을 경우 알림X
         * 대댓글에 태그당한 사람과 게시글 주인이 같을 경우
         * 
         * 태그가 있을 경우 -> mention != null
         * 1. 태그된 사람 == 게시글 작성자
         *    게시글 작성자에게 댓글 알림을 준다.
         *
         * 2. 태그된 사람 != 게시글 작성자
         *    게시글 작성자에게는 댓글 알림
         *    태그된 사람에게는 태그 알림
         *
         * 태그가 없는 경우 -> 원댓글인 경우 -> mention == null
         *    게시글 작성자에게만 알림을 준다.
         *    
         *  댓글 단 사람 == user
         *  게시글의 주인 == review.getUser
         *  태그 된 사람 == createCommentsRequest.getMention 을 통해 찾은 사람
         */

        String reviewUserIdx = review.getUser().getUserIdx().toString();
        String notificationId;
        if (createCommentsRequest.getMention() != null) { // 멘션이 달렸을 경우 (태그O)
            String mention = createCommentsRequest.getMention();
            String mentionUserIdx = userRepository.findByNickName(mention)
                    .orElseThrow().getUserIdx()
                    .toString(); //태그된 사람의 Idx
            log.info("mention = {}", mention);
            /**
             * 일단 태그가 이루어짐
             * 1. 게시글 알림 (무조건 감)
             *      댓글 작성자가 게시글 작성자인 경우 제외
             * 2. 태그 알림
             *      1. 게시판 작성자가 아니면서 본인이 본인을 태그한 경우가 아니어야 함
             */
            if (!mentionUserIdx.equals(reviewUserIdx))  {
                // 태그된 사용자 != 게시글 작성자
                if (Long.parseLong(mentionUserIdx) != user.getUserIdx()) {
                    //댓글 작성자 != 태그된 사용자
                    notificationId = UUID.randomUUID().toString();
                    redisRepository.addNotification(
                            mentionUserIdx,
                            notificationId,
                            review.getTitle(),
                            "Review",
                            reviewIdx.toString(),
                            user.getNickName(),
                            createCommentsRequest.getContent(),
                            mention,
                            LocalDateTime.now()
                    ); //태그 알림
                }
            }
            if (Long.parseLong(reviewUserIdx) == user.getUserIdx())
                return;

            log.info("작성자와 태그된 사람이 같음 -> 태그 알림 X");
            notificationId = UUID.randomUUID().toString();
            redisRepository.addNotification(
                    reviewUserIdx,
                    notificationId,
                    review.getTitle(),
                    "Review",
                    reviewIdx.toString(),
                    user.getNickName(),
                    createCommentsRequest.getContent(),
                    null,
                    LocalDateTime.now()
            ); //게시글 알림

            ReviewComments reviewComments;
            if (createCommentsRequest.getParentIdx() == null) {
                reviewComments = saveParentComments(createCommentsRequest, user, review);
            } else {
                reviewComments = saveChildComments(createCommentsRequest, user, review);
            }

            reviewCommentRepository.save(reviewComments);
            return;

        }  // 멘션이 달렸을 경우 (태그O) 완료

        // 멘션이 없는 경우
        notificationId = UUID.randomUUID().toString();
        if (user.getUserIdx() != Long.parseLong(reviewUserIdx)) {
            redisRepository.addNotification(
                    reviewUserIdx,
                    notificationId,
                    review.getTitle(),
                    "Review",
                    reviewIdx.toString(),
                    user.getNickName(),
                    createCommentsRequest.getContent(),
                    null,
                    LocalDateTime.now()
            );
        }

        ReviewComments reviewComments;
        if (createCommentsRequest.getParentIdx() == null) {
            reviewComments = saveParentComments(createCommentsRequest, user, review);
        } else {
            reviewComments = saveChildComments(createCommentsRequest, user, review);
        }

        reviewCommentRepository.save(reviewComments);
    }
    public void listenCommentSave(Long listenIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        ListenTogether listenTogether = listenTogetherRepository.findById(listenIdx).orElseThrow();

        ListenComments listenComments;
        if (createCommentsRequest.getParentIdx() == null) {
            listenComments = saveParentComments(createCommentsRequest, user, listenTogether);
        } else {
            listenComments = saveChildComments(createCommentsRequest, user, listenTogether);
        }

        listenCommentRepository.save(listenComments);
    }
    public void freeCommentSave(Long freeBoardIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardIdx).orElseThrow();

        FreeBoardComments freeBoardComments;
        if (createCommentsRequest.getParentIdx() == null) {
            freeBoardComments = saveParentComments(createCommentsRequest, user, freeBoard);
        } else {
            freeBoardComments = saveChildComments(createCommentsRequest, user, freeBoard);
        }

        freeBoardCommentRepository.save(freeBoardComments);
    }

    private ReviewComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, Review review) {
        Optional<ReviewComments> parent = reviewCommentRepository.findById(createCommentsRequest.getParentIdx());

        return ReviewComments.builder()
                .user(user)
                .review(review)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent.get())
                .build();
    }

    private ListenComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether) {
        Optional<ListenComments> parent = listenCommentRepository.findById(createCommentsRequest.getParentIdx());

        return ListenComments.builder()
                .user(user)
                .listenTogether(listenTogether)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent.get())
                .build();
    }
    private FreeBoardComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard) {
        Optional<FreeBoardComments> parent = freeBoardCommentRepository.findById(createCommentsRequest.getParentIdx());

        return FreeBoardComments.builder()
                .user(user)
                .freeBoard(freeBoard)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent.get())
                .build();
    }

    private ReviewComments saveParentComments(CreateCommentsRequest createCommentsRequest, User user, Review review) {
        return ReviewComments.builder()
                .user(user)
                .review(review)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .build();
    }
    private ListenComments saveParentComments(CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether) {
        return ListenComments.builder()
                .user(user)
                .listenTogether(listenTogether)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .build();
    }
    private FreeBoardComments saveParentComments(CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard) {
        return FreeBoardComments.builder()
                .user(user)
                .freeBoard(freeBoard)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .build();
    }

    public ReviewCommentResponse getReviewComments(Long reviewIdx) {
        List<ReviewComments> reviewComments = reviewCommentRepository.findAllByReview_ReviewIdxOrderByCreatedAtAsc(reviewIdx);
        cnt = 0;
        List<SelectReviewComment> selectReviewComment = new ArrayList<>();
        for (int i = 0; i < reviewComments.size(); i++) {
            List<ChildReviewComment> childReviewComments = getChildReviewComments(reviewComments.get(i));
            if (childReviewComments == null) continue;
//            selectReviewComment.add(SelectReviewComment.from(reviewComments.get(i), childReviewComments));
        }
        cnt += selectReviewComment.size();
        return ReviewCommentResponse.builder()
                .cnt(cnt)
                .reviewCommentList(selectReviewComment)
                .build();
    }

    @Nullable
    private List<ChildReviewComment> getChildReviewComments(ReviewComments reviewComments) {
        List<ChildReviewComment> childReviewComments = new ArrayList<>();
        List<ReviewComments> childs = reviewComments.getChild();
        if (reviewComments.getParent() != null)
            return null;
        for (int j = 0; j < childs.size(); j++) {
//            childReviewComments.add(ChildReviewComment.from(childs.get(j)));
        }
        cnt += childReviewComments.size();
        return childReviewComments;
    }

    public ListenCommentResponse getListenComments(Long listenIdx) {
        List<ListenComments> listenComments = listenCommentRepository.findAllByListenTogether_ListenIdxOrderByCreatedAtAsc(listenIdx);
        cnt = 0;
        List<SelectListenComment> selectListenComment = new ArrayList<>();
        for (int i = 0; i < listenComments.size(); i++) {
            List<ChildListenComment> childListenComments = getChildListenComments(listenComments.get(i));
            if (childListenComments == null) continue;
//            selectListenComment.add(SelectListenComment.from(listenComments.get(i), childListenComments));
        }
        cnt += selectListenComment.size();
        return ListenCommentResponse.builder()
                .cnt(cnt)
                .listenCommentList(selectListenComment).build();
    }

    @Nullable
    private List<ChildListenComment> getChildListenComments(ListenComments listenComments) {
        List<ChildListenComment> childListenComments = new ArrayList<>();
        List<ListenComments> childs = listenComments.getChild();
        if (listenComments.getParent() != null)
            return null;
        for (int j = 0; j < childs.size(); j++) {
//            childListenComments.add(ChildListenComment.from(childs.get(j)));
        }
        cnt += childListenComments.size();
        return childListenComments;
    }

    public FreeCommentResponse getFreeComments(Long freeBoardIdx) {
        List<FreeBoardComments> freeBoardComments = freeBoardCommentRepository.findAllByFreeBoard_FreeBoardIdxOrderByCreatedAtAsc(freeBoardIdx);
        cnt = 0;
        List<SelectFreeComment> selectFreeComment = new ArrayList<>();
        for (int i = 0; i < freeBoardComments.size(); i++) {
            List<ChildFreeComment> childFreeComments = getChildFreeComments(freeBoardComments.get(i));
            if (childFreeComments == null) continue;
//            selectFreeComment.add(SelectFreeComment.from(freeBoardComments.get(i), childFreeComments));
        }
        cnt += selectFreeComment.size();
        return FreeCommentResponse.builder()
                .cnt(cnt)
                .freeCommentList(selectFreeComment)
                .build();
    }

    @Nullable
    private List<ChildFreeComment> getChildFreeComments(FreeBoardComments freeBoardComments) {
        List<ChildFreeComment> childFreeComments = new ArrayList<>();
        List<FreeBoardComments> childs = freeBoardComments.getChild();
        if (freeBoardComments.getParent() != null)
            return null;
        for (int j = 0; j < childs.size(); j++) {
//            childFreeComments.add(ChildFreeComment.from(childs.get(j)));
        }
        cnt += childFreeComments.size();
        return childFreeComments;
    }
}
