package chilling.encore.dto;

import lombok.Builder;
import lombok.Getter;

public abstract class PopularPostDto {

    @Getter
    @Builder
    public static class PopularListenPost {
        private Long listenIdx;
        private String title;

        public PopularListenPost from(Long listenIdx, String title) {
            return PopularListenPost.builder()
                    .listenIdx(listenIdx)
                    .title(title)
                    .build();
        }
    }
}
