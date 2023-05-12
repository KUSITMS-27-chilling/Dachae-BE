package chilling.encore.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class CommentsDto {
    @Getter
    @RequiredArgsConstructor
    @ApiModel(description = "댓글을 위한 요청 객체")
    public static class CreateCommentsRequest {
        int ref;
        int refOrder;
        int step;
        Long parentIdx;
        String content;
        int childSum;
    }
}
