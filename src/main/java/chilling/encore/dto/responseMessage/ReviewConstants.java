package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ReviewConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ReviewSuccessMessage {
        REVIEW_SUCCESS_MESSAGE("수강후기 조회에 성공했습니다."),
        POPULAR_REVIEW_SUCCESS_MESSAGE("인기 수강후기 조회에 성공했습니다."),
        CREATE_SUCCESS_MESSAGE("수강후기 생성에 성공했습니다"),
        CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE("수강후기 댓글 생성에 성공했습니다"),
        REVIEW_DETAIL_SUCCESS_MESSAGE("수강후기 상세정보 조회에 성공했습니다"),
        MY_REVIEW_SUCCESS_MESSAGE("나의 수강후기 조회에 성공했습니다");
        private final String message;
    }
}
