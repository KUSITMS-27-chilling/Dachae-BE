package chilling.encore.controller;

import chilling.encore.dto.ReviewDto.*;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;


import java.util.List;

import static chilling.encore.dto.responseMessage.ReviewConstants.ReviewSuccessMessage.*;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;
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
    @ApiOperation(value = "후기 인기글 top3 조회", notes = "로그인 안 한 경우 인기지역 4개에서 top3")
    public ResponseEntity<ResponseDto<PopularReviewPage>> getPopularReview() {
        PopularReviewPage popularReviews = reviewService.getPopularReview();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), POPULAR_REVIEW_SUCCESS_MESSAGE.getMessage(), popularReviews));
    }

    @PostMapping("/save")
    @ApiOperation(value = "수강 후기 글 작성", notes = "programIdx 함께 넘겨주세요")
    public ResponseEntity<ResponseDto> save(@RequestBody CreateReviewRequest createReviewRequest) {
        reviewService.save(createReviewRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_SUCCESS_MESSAGE.getMessage()));
    }

    @GetMapping("/{reviewIdx}")
    @ApiOperation(value = "수강 후기 상세보기", notes = "게시글 클릭 시 상세조회 부분")
    public ResponseEntity<ResponseDto<SelectReview>> getReviewDetail(@PathVariable Long reviewIdx) {
        SelectReview review = reviewService.getReview(reviewIdx);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), REVIEW_DETAIL_SUCCESS_MESSAGE.getMessage(), review));
    }

    @GetMapping("/mine")
    @ApiOperation(value = "나의 수강후기", notes = "작성시간 내림차순으로 top2 조회")
    public ResponseEntity<ResponseDto<List<SelectMyReview>>> getMyReview() {
        List<SelectMyReview> myReview = reviewService.getMyReview();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), MY_REVIEW_SUCCESS_MESSAGE.getMessage(), myReview));
    }
}
