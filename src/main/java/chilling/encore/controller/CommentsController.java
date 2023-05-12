package chilling.encore.controller;

import chilling.encore.dto.ReviewCommentsDto.CreateReviewCommentsRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.ReviewConstants.ReviewSuccessMessage.CREATE_SUCCESS_MESSAGE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Comments API")
public class CommentsController {
    private final CommentsService commentsService;
    @PostMapping("/review/{reviewIdx}/comments")
    @ApiOperation(value = "수강후기 댓글 작성", notes = "reviewIdx, ref, refOrder, step 함께 넘겨주세요")
    public ResponseEntity<ResponseDto> save(@RequestBody CreateReviewCommentsRequest createReviewCommentsRequest) {
        commentsService.save(createReviewCommentsRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_SUCCESS_MESSAGE.getMessage()));
    }
}
