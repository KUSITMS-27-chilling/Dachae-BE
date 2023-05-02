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
        private int favCount;
        private List<ListenPosts> listenPosts;

        public PopularListenPostsResponse from(int favCount, List<ListenPosts> listenPosts) {
            return PopularListenPostsResponse.builder()
                    .listenPosts(listenPosts)
                    .favCount(favCount)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ListenPosts {
        private Long listenInx;
        private String title;
        private int goalNum;
        private User user;
        private Program program;

        public ListenPosts from(ListenTogether listenTogether) {
            return ListenPosts.builder()
                    .listenInx(listenInx)
                    .title(title)
                    .goalNum(goalNum)
                    .user(user)
                    .program(program)
                    .build();
        }
    }
}
