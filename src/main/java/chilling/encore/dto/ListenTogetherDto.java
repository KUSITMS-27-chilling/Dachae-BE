package chilling.encore.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public abstract class ListenTogetherDto {

    @Getter
    @Builder
    @ApiModel(description = "같이들어요 접근을 위한 응답객체")
    public static class ListenTogetherResponse {
        private List<PopularPostDto.PopularListenPost> popularListenPosts;

        public static ListenTogetherResponse from(
                List<PopularPostDto.PopularListenPost> popularListenPosts
        ) {
            return ListenTogetherResponse.builder()
                    .popularListenPosts(popularListenPosts)
                    .build();
        }
    }
}
