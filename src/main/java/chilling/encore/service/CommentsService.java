package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto.*;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenCommentRepository;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ReviewCommentRepository;
import chilling.encore.repository.springDataJpa.ReviewRepository;
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

    public List<ReviewCommentResponse> getReviewComments(Long reviewIdx) {
        List<ReviewComments> reviewComments = reviewCommentRepository.findAllByReview_ReviewIdxOrderByCreatedAtAsc(reviewIdx);

        List<ReviewCommentResponse> reviewCommentResponses = new ArrayList<>();
        for (int i = 0; i < reviewComments.size(); i++) {
            List<ChildReviewComment> childReviewComments = getChildReviewComments(reviewComments.get(i));
            if (childReviewComments == null) continue;
            reviewCommentResponses.add(ReviewCommentResponse.from(reviewComments.get(i), childReviewComments));
        }
        return reviewCommentResponses;
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
        return childReviewComments;
    }

    public List<ListenCommentResponse> getListenComments(Long listenIdx) {
        List<ListenComments> listenComments = listenCommentRepository.findAllByListenTogether_ListenIdxOrderByCreatedAtAsc(listenIdx);

        List<ListenCommentResponse> listenCommentResponse = new ArrayList<>();
        for (int i = 0; i < listenComments.size(); i++) {
            List<ChildListenComment> childListenComments = getChildListenComments(listenComments.get(i));
            if (childListenComments == null) continue;
            listenCommentResponse.add(ListenCommentResponse.from(listenComments.get(i), childListenComments));
        }
        return listenCommentResponse;
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
        return childListenComments;
    }


}
