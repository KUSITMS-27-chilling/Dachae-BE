package chilling.encore.dto;

import chilling.encore.domain.Review;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public abstract class ReviewCommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "수강후기 댓글을 위한 요청 객체")
    public static class CreateReviewCommentsRequest {
        Long reviewIdx;
        int ref;
        int refOrder;
        int step;
        Long parentIdx;
        String content;
        int childSum;
    }
}
