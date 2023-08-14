package chilling.encore.domain.freeBoard.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FreeBoardConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        FREE_SELECT_SUCCESS("자유게시판 조회에 성공했습니다."),
        FREE_DETAIL_SUCCESS("자유게시판 상세 조회에 성공했습니다."),
        POPULAR_SELECT_SUCCESS("자유게시판 인기 게시물 조회에 성공했습니다."),
        FREE_CREATE_SUCCESS("자유게시판 생성에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum FreeBoardFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 게시글 번호가 잘못되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum FreeBoardFailCode {
        NO_SUCH_IDX_CODE("fr001");
        private final String code;
    }
}
