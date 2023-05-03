package chilling.encore.dto;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public abstract class ListenTogetherPostDto {
    @Getter
    @Builder
    @ApiModel(description = "인기 같이들어요 3개 보여주기 위한 응답 객체")
    public static class PopularListenPostsResponse {
        private List<ListenTogetherPosts> listenTogetherPosts;

        public static PopularListenPostsResponse from(List<ListenTogetherPosts> listenTogetherPosts) {
            return PopularListenPostsResponse.builder()
                    .listenTogetherPosts(listenTogetherPosts)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ListenTogetherPosts {
        private Long listenInx;
        private String title;
        private int goalNum;
        private int hit;
        private User user;
        private Program program;

        public static ListenTogetherPosts from(ListenTogether listenTogether) {
            return ListenTogetherPosts.builder()
                    .listenInx(listenTogether.getListenIdx())
                    .title(listenTogether.getTitle())
                    .goalNum(listenTogether.getGoalNum())
                    .hit(listenTogether.getHit())
                    .user(listenTogether.getUser())
                    .program(listenTogether.getProgram())
                    .build();
        }
    }
}
