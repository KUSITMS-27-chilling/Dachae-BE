package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto;
import chilling.encore.dto.CommentsDto.ChildReviewComment;
import chilling.encore.dto.CommentsDto.CreateReviewCommentsRequest;
import chilling.encore.dto.CommentsDto.ListenCommentResponse;
import chilling.encore.dto.CommentsDto.ReviewCommentResponse;
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

    public void reviewCommentSave(Long reviewIdx, CreateReviewCommentsRequest createReviewCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findById(reviewIdx).orElseThrow();

        ReviewComments reviewComments;
        if (createReviewCommentsRequest.getParentIdx() == null) {
            reviewComments = saveParentReviewComments(createReviewCommentsRequest, user, review);
        } else {
            reviewComments = saveChildReviewComments(createReviewCommentsRequest, user, review);
        }

        reviewCommentRepository.save(reviewComments);
    }

    private ReviewComments saveChildReviewComments(CreateReviewCommentsRequest createReviewCommentsRequest, User user, Review review) {
        ReviewComments reviewComments;
        Optional<ReviewComments> parent = reviewCommentRepository.findById(createReviewCommentsRequest.getParentIdx());

        reviewComments = ReviewComments.builder()
                .user(user)
                .review(review)
                .isDelete(false)
                .content(createReviewCommentsRequest.getContent())
                .parent(parent.get())
                .build();
        return reviewComments;
    }

    private ReviewComments saveParentReviewComments(CreateReviewCommentsRequest createReviewCommentsRequest, User user, Review review) {
        ReviewComments reviewComments;
        reviewComments = ReviewComments.builder()
                .user(user)
                .review(review)
                .isDelete(false)
                .content(createReviewCommentsRequest.getContent())
                .build();
        return reviewComments;
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

    public void listenCommentSave(Long listenIdx, CommentsDto.CreateListenCommentsRequest createListenCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        ListenTogether listenTogether = listenTogetherRepository.findByListenIdx(listenIdx);

        ListenComments listenComments = ListenComments.builder()
                .user(user)
                .listenTogether(listenTogether)
                .ref(createListenCommentsRequest.getRef())
                .refOrder(createListenCommentsRequest.getRefOrder())
                .childSum(createListenCommentsRequest.getChildSum())
                .content(createListenCommentsRequest.getContent())
                .build();

        listenCommentRepository.save(listenComments);
    }

    public List<ListenCommentResponse> getListenComments(Long listenIdx) {
        ListenTogether listenTogether = listenTogetherRepository.findByListenIdx(listenIdx);
        List<ListenComments> listenComments = listenTogether.getListenComments();
        List<ListenCommentResponse> listenCommentResponseList = new ArrayList<>();

        for (ListenComments listenComment : listenComments) {
            listenCommentResponseList.add(ListenCommentResponse.from(listenComment));
        }
        return listenCommentResponseList;
    }


}
