package chilling.encore.dto;

import lombok.Builder;
import lombok.Getter;

public abstract class AlaramDto {
    @Getter
    @Builder
    public static class NewAlarm {
        private Long listenCommentsIdx;
        private Long reviewCommentIdx;
        private String nickName;

        public NewAlarm from(Long listenCommentsIdx, Long reviewCommentIdx, String nickName) {
            return NewAlarm.builder()
                    .listenCommentsIdx(listenCommentsIdx)
                    .reviewCommentIdx(reviewCommentIdx)
                    .nickName(nickName)
                    .build();
        }
    }
}
