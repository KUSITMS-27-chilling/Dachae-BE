package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenCommentRepository;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ReviewCommentRepository;
import chilling.encore.repository.springDataJpa.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final ListenCommentRepository listenCommentRepository;
    private final ListenTogetherRepository listenTogetherRepository;

    public void reviewCommentSave(Long reviewIdx, CreateCommentsRequest createCommentsRequest) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findByReviewIdx(reviewIdx);

        ReviewComments reviewComments = ReviewComments.builder()
                .user(securityUser)
                .review(review)
                .ref(createCommentsRequest.getRef())
                .refOrder(createCommentsRequest.getRefOrder())
                .step(createCommentsRequest.getStep())
                .childSum(createCommentsRequest.getChildSum())
                .content(createCommentsRequest.getContent())
                .build();


        reviewCommentRepository.save(reviewComments);
    }


    public void listenCommentSave(Long listenIdx, CreateCommentsRequest createCommentsRequest) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        ListenTogether listenTogether = listenTogetherRepository.findByListenIdx(listenIdx);

        ListenComments listenComments = ListenComments.builder()
                .user(securityUser)
                .listenTogether(listenTogether)
                .ref(createCommentsRequest.getRef())
                .refOrder(createCommentsRequest.getRefOrder())
                .step(createCommentsRequest.getStep())
                .childSum(createCommentsRequest.getChildSum())
                .content(createCommentsRequest.getContent())
                .build();

        listenCommentRepository.save(listenComments);
    }
}
