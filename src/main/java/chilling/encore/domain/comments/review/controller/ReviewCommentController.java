package chilling.encore.domain.comments.review.controller;

import chilling.encore.domain.comments.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.comments.review.service.ReviewCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.domain.comments.dto.CommentsDto.*;
import static chilling.encore.domain.comments.constant.CommentsConstants.CommentsSuccessMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "ReviewComments API")
@RequestMapping("/review")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;
    @PostMapping("/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 작성")
    public ResponseEntity<ResponseDto> reviewCommentSave(
            @PathVariable Long reviewIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        reviewCommentService.reviewCommentSave(reviewIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 조회")
    public ResponseEntity<ResponseDto<ReviewCommentResponse>> getReviewComment(@PathVariable Long reviewIdx) {
        ReviewCommentResponse reviewComments = reviewCommentService.getReviewComments(reviewIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage(), reviewComments));
    }
}
