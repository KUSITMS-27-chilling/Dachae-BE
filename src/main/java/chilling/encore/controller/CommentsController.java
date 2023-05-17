package chilling.encore.controller;

import chilling.encore.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "수강후기 댓글 작성")
    public ResponseEntity<ResponseDto> reviewCommentSave(
            @PathVariable Long reviewIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        commentsService.reviewCommentSave(reviewIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/review/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 조회")
    public ResponseEntity<ResponseDto<ReviewCommentResponse>> getReviewComment(@PathVariable Long reviewIdx) {
        ReviewCommentResponse reviewComments = commentsService.getReviewComments(reviewIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage(), reviewComments));
    }

    @PostMapping("/listen/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 작성")
    public ResponseEntity<ResponseDto> listenCommentSave(
            @PathVariable Long listenIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        commentsService.listenCommentSave(listenIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/listen/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 조회")
    public ResponseEntity<ResponseDto<ListenCommentResponse>> getListenComment(@PathVariable Long listenIdx) {
        ListenCommentResponse listenComments = commentsService.getListenComments(listenIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage(), listenComments));
    }

    @PostMapping("/free/{freeBoardIdx}/comments")
    @ApiOperation(value = "자유게시판 댓글 작성")
    public ResponseEntity<ResponseDto> freeCommentSave(
            @PathVariable Long freeBoardIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        commentsService.freeCommentSave(freeBoardIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_FREE_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/free/{freeBoardIdx}/comments")
    @ApiOperation(value = "자유게시판 댓글 조회")
    public ResponseEntity<ResponseDto<FreeCommentResponse>> getFreeComment(@PathVariable Long freeBoardIdx) {
        FreeCommentResponse freeComments = commentsService.getFreeComments(freeBoardIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_FREE_COMMENT_SUCCESS_MESSAGE.getMessage(), freeComments));
    }
}
