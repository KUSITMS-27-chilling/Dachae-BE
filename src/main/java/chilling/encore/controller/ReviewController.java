package chilling.encore.controller;

import chilling.encore.dto.ReviewDto.PopularReviewPage;
import chilling.encore.dto.ReviewDto.ReviewPage;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.annotation.Nullable;


import static chilling.encore.dto.responseMessage.ReviewConstants.ReviewSuccessMessage.POPULAR_REVIEW_SUCCESS_MESSAGE;
import static chilling.encore.dto.responseMessage.ReviewConstants.ReviewSuccessMessage.REVIEW_SUCCESS_MESSAGE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
@Api(tags = "ReviewPage API")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping(value={"/page/{region}/{page}", "/page/{region}"})
    @ApiOperation(value = "후기 페이징 조회")
    public ResponseEntity<ResponseDto<ReviewPage>> getReviewPage(@PathVariable String region, @PathVariable @Nullable Integer page) {
        ReviewPage reviewPage = reviewService.getReviewPage(region, page);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), REVIEW_SUCCESS_MESSAGE.getMessage(), reviewPage));
    }

    @GetMapping("/popular")
    @ApiOperation(value = "후기 인기글 조회")
    public ResponseEntity<ResponseDto<PopularReviewPage>> getPopularReview() {
        PopularReviewPage popularReviews = reviewService.getPopularReview();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), POPULAR_REVIEW_SUCCESS_MESSAGE.getMessage(), popularReviews));
    }
}
