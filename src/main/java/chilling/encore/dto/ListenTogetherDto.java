package chilling.encore.dto;

import chilling.encore.dto.ListenTogetherPostDto.ListenPosts;
import chilling.encore.dto.ListenTogetherPostDto.PopularListenPostsResponse;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class ListenTogetherDto {

    @Getter
    @Builder
    @ApiModel(description = "같이들어요 접근을 위한 응답객체")
    public static class ListenTogetherResponse {

        private Map<String, PopularListenPostsResponse> popularListenPosts;
        private List<ListenPosts> listenPosts;
        // 내가 작성한 제안할래요 글 추가필요

        public static ListenTogetherResponse from(
                Map<String, PopularListenPostsResponse> popularListenPosts,
                List<ListenPosts> listenPosts
        ) {
            return ListenTogetherResponse.builder()
                    .popularListenPosts(popularListenPosts)
                    .listenPosts(listenPosts)
                    .build();
        }
    }
}
