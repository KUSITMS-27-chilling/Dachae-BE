package chilling.encore.service;

import chilling.encore.domain.Review;
import chilling.encore.domain.ReviewComments;
import chilling.encore.domain.User;
import chilling.encore.dto.ReviewCommentsDto;
import chilling.encore.dto.ReviewCommentsDto.CreateReviewCommentsRequest;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenCommentRepository;
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

    public void save(CreateReviewCommentsRequest createCommentsRequest) {
        User securityUser = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Review review = reviewRepository.findByReviewIdx(createCommentsRequest.getReviewIdx());

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

}
