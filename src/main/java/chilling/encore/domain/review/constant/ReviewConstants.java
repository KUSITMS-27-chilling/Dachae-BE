package chilling.encore.domain.review.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ReviewConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ReviewSuccessMessage {
        REVIEW_SUCCESS_MESSAGE("수강후기 조회에 성공했습니다."),
        POPULAR_REVIEW_SUCCESS_MESSAGE("인기 수강후기 조회에 성공했습니다."),
        CREATE_SUCCESS_MESSAGE("수강후기 생성에 성공했습니다"),
        REVIEW_DETAIL_SUCCESS_MESSAGE("수강후기 상세정보 조회에 성공했습니다"),
        MY_REVIEW_SUCCESS_MESSAGE("나의 수강후기 조회에 성공했습니다");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ReviewFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 게시글 번호가 잘못되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ReviewFailCode {
        NO_SUCH_IDX_CODE("rv001");
        private final String code;
    }
}
