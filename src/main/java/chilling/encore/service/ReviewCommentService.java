package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto.*;
import chilling.encore.exception.ReviewException.NoSuchIdxException;
import chilling.encore.exception.UserException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;

    private int cnt;
    private final String REVIEW = "Review";
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
    public void reviewCommentSave(Long reviewIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        if (isMention(reviewIdx, createCommentsRequest, user))
            return;
        // 멘션이 없는 경우
        notMention(reviewIdx, createCommentsRequest, user);
    }

    private boolean isMention(Long reviewIdx, CreateCommentsRequest createCommentsRequest, User user) {
        if (createCommentsRequest.getMention() != null) { // 멘션이 달렸을 경우 (태그O)
            Review review = reviewRepository.findById(reviewIdx).orElseThrow(() -> new NoSuchIdxException());
            String reviewUserIdx = review.getUser().getUserIdx().toString();
            String mention = createCommentsRequest.getMention();
            String mentionUserIdx = userRepository.findByNickName(mention)
                    .orElseThrow(() -> new UserException.NoSuchRegionException()).getUserIdx()
                    .toString(); //태그된 사람의 Idx

            validMention(reviewIdx, createCommentsRequest, user, review, reviewUserIdx, mention, mentionUserIdx);

            if (Long.parseLong(reviewUserIdx) == user.getUserIdx())
                return true;

            addCommentToRedis(reviewIdx, createCommentsRequest, user, review, reviewUserIdx, REVIEW, mention);

            saveComments(createCommentsRequest, user, review);
            return true;

        }  // 멘션이 달렸을 경우 (태그O) 완료
        return false;
    }

    private void validMention(Long reviewIdx, CreateCommentsRequest createCommentsRequest, User user, Review review, String reviewUserIdx, String mention, String mentionUserIdx) {
        if (!mentionUserIdx.equals(reviewUserIdx))  {
            // 태그된 사용자 != 게시글 작성자
            if (Long.parseLong(mentionUserIdx) != user.getUserIdx()) {
                //댓글 작성자 != 태그된 사용자
                addCommentToRedis(reviewIdx, createCommentsRequest, user, review, reviewUserIdx, REVIEW, mention);
            }
        }
    }

    private void notMention(Long reviewIdx, CreateCommentsRequest createCommentsRequest, User user) {
        Review review = reviewRepository.findById(reviewIdx).orElseThrow(() -> new NoSuchIdxException());
        String reviewUserIdx = review.getUser().getUserIdx().toString();
        if (user.getUserIdx() != Long.parseLong(reviewUserIdx)) {
            addCommentToRedis(reviewIdx, createCommentsRequest, user, review, reviewUserIdx, REVIEW, null);
        }

        saveComments(createCommentsRequest, user, review);
    }

    private void addCommentToRedis(Long reviewIdx, CreateCommentsRequest createCommentsRequest, User user, Review review, String reviewUserIdx, String boardType, String mention) {
        redisRepository.addNotification(
                reviewUserIdx,
                UUID.randomUUID().toString(),
                review.getTitle(),
                boardType,
                reviewIdx.toString(),
                user.getNickName(),
                createCommentsRequest.getContent(),
                mention,
                LocalDateTime.now()
        ); //게시글 알림
    }

    private void saveComments(CreateCommentsRequest createCommentsRequest, User user, Review review) {
        ReviewComments reviewComments;
        if (createCommentsRequest.getParentIdx() == null) {
            reviewComments = saveParentComments(createCommentsRequest, user, review);
        } else {
            reviewComments = saveChildComments(createCommentsRequest, user, review);
        }

        reviewCommentRepository.save(reviewComments);
    }

    private ReviewComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, Review review) {
        ReviewComments parent = reviewCommentRepository.findById(createCommentsRequest.getParentIdx()).orElseThrow(() -> new NoSuchIdxException());

        return ReviewComments.builder()
                .user(user)
                .review(review)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent)
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
}
