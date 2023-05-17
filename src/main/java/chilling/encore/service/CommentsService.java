package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto.*;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private int cnt;

    public void reviewCommentSave(Long reviewIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findById(reviewIdx).orElseThrow();

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
            selectReviewComment.add(SelectReviewComment.from(reviewComments.get(i), childReviewComments));
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
            childReviewComments.add(ChildReviewComment.from(childs.get(j)));
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
            selectListenComment.add(SelectListenComment.from(listenComments.get(i), childListenComments));
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
            childListenComments.add(ChildListenComment.from(childs.get(j)));
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
            selectFreeComment.add(SelectFreeComment.from(freeBoardComments.get(i), childFreeComments));
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
            childFreeComments.add(ChildFreeComment.from(childs.get(j)));
        }
        cnt += childFreeComments.size();
        return childFreeComments;
    }
}
