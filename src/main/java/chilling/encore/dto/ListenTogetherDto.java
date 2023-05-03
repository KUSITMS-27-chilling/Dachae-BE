package chilling.encore.dto;

import chilling.encore.dto.ListenTogetherPostDto.ListenTogetherPosts;
import chilling.encore.dto.ListenTogetherPostDto.PopularListenPostsResponse;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public abstract class ListenTogetherDto {

    @Getter
    @Builder
    @ApiModel(description = "같이들어요 접근을 위한 응답객체")
    public static class ListenTogetherResponse {

        private PopularListenPostsResponse popularListenPosts;
        // 인기글
        private List<ListenTogetherPosts> listenTogetherPosts;
        // 같이들어요 리스트
        // 내가 작성한 제안할래요 글 추가필요

        public static ListenTogetherResponse from(
                PopularListenPostsResponse popularListenPosts,
                List<ListenTogetherPosts> listenTogetherPosts
        ) {
            return ListenTogetherResponse.builder()
                    .popularListenPosts(popularListenPosts)
                    .listenTogetherPosts(listenTogetherPosts)
                    .build();
        }
    }
}
