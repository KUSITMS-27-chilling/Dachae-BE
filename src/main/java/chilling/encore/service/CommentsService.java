package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto;
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
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findByReviewIdx(reviewIdx);

        ReviewComments reviewComments;
        if (createReviewCommentsRequest.getParentIdx() == 0) {
            reviewComments = ReviewComments.builder()
                    .user(securityUser)
                    .review(review)
                    .ref(createReviewCommentsRequest.getRef())
                    .refOrder(createReviewCommentsRequest.getRefOrder())
                    .step(createReviewCommentsRequest.getStep())
                    .childSum(createReviewCommentsRequest.getChildSum())
                    .content(createReviewCommentsRequest.getContent())
                    .build();
        } else {
            Optional<ReviewComments> parent = reviewCommentRepository.findById(createReviewCommentsRequest.getParentIdx());

            reviewComments = ReviewComments.builder()
                    .user(securityUser)
                    .review(review)
                    .ref(createReviewCommentsRequest.getRef())
                    .refOrder(createReviewCommentsRequest.getRefOrder())
                    .step(createReviewCommentsRequest.getStep())
                    .childSum(createReviewCommentsRequest.getChildSum())
                    .content(createReviewCommentsRequest.getContent())
                    .parent(parent.get())
                    .build();
        }

        reviewCommentRepository.save(reviewComments);
    }


    public void listenCommentSave(Long listenIdx, CommentsDto.CreateListenCommentsRequest createListenCommentsRequest) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        ListenTogether listenTogether = listenTogetherRepository.findByListenIdx(listenIdx);

        ListenComments listenComments = ListenComments.builder()
                .user(securityUser)
                .listenTogether(listenTogether)
                .ref(createListenCommentsRequest.getRef())
                .refOrder(createListenCommentsRequest.getRefOrder())
                .step(createListenCommentsRequest.getStep())
                .childSum(createListenCommentsRequest.getChildSum())
                .content(createListenCommentsRequest.getContent())
                .build();

        listenCommentRepository.save(listenComments);
    }

    public List<ReviewCommentResponse> getReviewComments(Long reviewIdx) {
        Review review = reviewRepository.findByReviewIdx(reviewIdx);
        List<ReviewComments> reviewComments = review.getReviewComments();
        List<ReviewCommentResponse> reviewCommentResponseList = new ArrayList<>();

        for (ReviewComments reviewComment : reviewComments) {
            reviewCommentResponseList.add(ReviewCommentResponse.from(reviewComment));
        }
        return reviewCommentResponseList;
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
