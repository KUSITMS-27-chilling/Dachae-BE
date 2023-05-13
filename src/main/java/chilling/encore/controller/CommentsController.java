package chilling.encore.controller;

import chilling.encore.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.ListenTogetherConstants.ListenTogetherSuccessMessage.*;
import static chilling.encore.dto.responseMessage.ReviewConstants.ReviewSuccessMessage.*;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Comments API")
public class CommentsController {
    private final CommentsService commentsService;
    @PostMapping("/review/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 작성", notes = "ref, refOrder, step 함께 넘겨주세요")
    public ResponseEntity<ResponseDto> ReviewCommentSave(
            @PathVariable Long reviewIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        commentsService.reviewCommentSave(reviewIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @PostMapping("/listen/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 작성", notes = "ref, refOrder, step 함께 넘겨주세요")
    public ResponseEntity<ResponseDto> ListenCommentSave(
            @PathVariable Long listenIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        commentsService.listenCommentSave(listenIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

}
