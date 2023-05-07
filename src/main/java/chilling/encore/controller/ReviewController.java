package chilling.encore.controller;

import chilling.encore.dto.ReviewDto;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
@Api(tags = "ReviewPage API")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping({"/page/{region}/{page}", "/page/{region}"})
    @ApiOperation(value = "후기 페이징 조회")
    public ResponseEntity<ResponseDto<ReviewPage>> getReviewTogetherPage(@PathVariable String region, @PathVariable @Nullable Integer page) {
        ReviewPage reviewPage = reviewService.getReviewPage(region, page);
        return ResponseEntity.ok(ResponseDto.create(1, "", reviewPage));
    }
}
