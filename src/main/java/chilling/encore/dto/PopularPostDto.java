package chilling.encore.dto;

import lombok.Builder;
import lombok.Getter;

public abstract class PopularPostDto {

    @Getter
    @Builder
    public static class PopularListenPost {
        private Long listenIdx;
        private String title;
        private int hit;

        public PopularListenPost from(Long listenIdx, String title, int hit) {
            return PopularListenPost.builder()
                    .listenIdx(listenIdx)
                    .title(title)
                    .hit(hit)
                    .build();
        }
    }
}
