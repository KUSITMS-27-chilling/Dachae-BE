package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ReviewConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ReviewSuccessMessage {
        REVIEW_SUCCESS_MESSAGE("수강후기 조회에 성공했습니다."),
        POPULAR_REVIEW_SUCCESS_MESSAGE("인기 수강후기 조회에 성공했습니다.");
        private final String message;
    }
}
