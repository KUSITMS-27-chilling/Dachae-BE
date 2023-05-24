package chilling.encore.controller.freeBoard;

import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.freeBoard.FreeBoardCommentService;
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
@Api(tags = "FreeBoarComment API")
public class FreeBoardCommentController {
    private final FreeBoardCommentService freeBoardCommentService;

    @PostMapping("/free/{freeBoardIdx}/comments")
    @ApiOperation(value = "자유게시판 댓글 작성")
    public ResponseEntity<ResponseDto> freeCommentSave(
            @PathVariable Long freeBoardIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        freeBoardCommentService.freeCommentSave(freeBoardIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_FREE_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/free/{freeBoardIdx}/comments")
    @ApiOperation(value = "자유게시판 댓글 조회")
    public ResponseEntity<ResponseDto<FreeCommentResponse>> getFreeComment(@PathVariable Long freeBoardIdx) {
        FreeCommentResponse freeComments = freeBoardCommentService.getFreeComments(freeBoardIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_FREE_COMMENT_SUCCESS_MESSAGE.getMessage(), freeComments));
    }
}
