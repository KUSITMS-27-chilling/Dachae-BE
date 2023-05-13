package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FreeBoardConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        FREE_SELECT_SUCCESS("자유게시판 조회에 성공했습니다."),
        POPULAR_SELECT_SUCCESS("자유게시판 인기 게시물 조회에 성공했습니다."),
        FREE_CREATE_SUCCESS("자유게시판 생성에 성공했습니다.");
        private final String message;
    }
}
