package chilling.encore.domain.comments.listenTogether.controller;

import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.comments.listenTogether.sevice.ListenCommentService;
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
@Api(tags = "ListenComments API")
@RequestMapping("/listen")
public class ListenTogetherCommentController {
    private final ListenCommentService listenCommentService;

    @PostMapping("/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 작성")
    public ResponseEntity<ResponseDto> listenCommentSave(
            @PathVariable Long listenIdx,
            @RequestBody CreateCommentsRequest createCommentsRequest) {
        listenCommentService.listenCommentSave(listenIdx, createCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), CREATE_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/{listenIdx}/comments")
    @ApiOperation(value = "같이들어요 댓글 조회")
    public ResponseEntity<ResponseDto<ListenCommentResponse>> getListenComment(@PathVariable Long listenIdx) {
        ListenCommentResponse listenComments = listenCommentService.getListenComments(listenIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_LISTEN_COMMENT_SUCCESS_MESSAGE.getMessage(), listenComments));
    }
}
