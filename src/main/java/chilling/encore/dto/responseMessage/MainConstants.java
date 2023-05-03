package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MainConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        MAIN_SELECT_SUCCESS("메인페이지 조회에 성공했습니다."),
        CHANGE_NEWPROGRAM_SUCCESS("새로운 프로그램 조회에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UserExceptionList {
//        REFRESH_TOKEN_ERROR("U0004", HttpStatus.BAD_REQUEST, "RefreshToken이 잘못되었습니다.");
//
//        private final String errorCode;
//        private final HttpStatus httpStatus;
//        private final String message;
    }
}
