package chilling.encore.controller;

import chilling.encore.dto.CommentsDto.CreateReviewCommentsRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static chilling.encore.dto.CommentsDto.*;
import static chilling.encore.dto.responseMessage.CommentsConstants.CommentsSuccessMessage.*;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Comments API")
public class CommentsController {
    private final CommentsService commentsService;

    @PostMapping("/review/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 작성", notes = "대댓글은 아직 로직 고민중...")
    public ResponseEntity<ResponseDto> reviewCommentSave(
            @PathVariable Long reviewIdx,
            @RequestBody CreateReviewCommentsRequest createReviewCommentsRequest) {
        commentsService.reviewCommentSave(reviewIdx, createReviewCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @PostMapping("/listen/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 작성", notes = "대댓글은 아직 로직 고민중...")
    public ResponseEntity<ResponseDto> listenCommentSave(
            @PathVariable Long listenIdx,
            @RequestBody CreateListenCommentsRequest createListenCommentsRequest) {
        commentsService.listenCommentSave(listenIdx, createListenCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/review/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 조회", notes = "대댓글은 아직 로직 고민중...")
    public ResponseEntity<ResponseDto<List<ReviewCommentResponse>>> getReviewComment(@PathVariable Long reviewIdx) {
        List<ReviewCommentResponse> reviewComments = commentsService.getReviewComments(reviewIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage(), reviewComments));
    }
    
    @GetMapping("/listen/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 조회", notes = "대댓글은 아직 로직 고민중...")
    public ResponseEntity<ResponseDto<List<ListenCommentResponse>>> getListenComment(@PathVariable Long listenIdx) {
        List<ListenCommentResponse> listenComments = commentsService.getListenComments(listenIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage(), listenComments));
    }
}
