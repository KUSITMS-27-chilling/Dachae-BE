package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ProgramConstant {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        SELECT_NEW_SUCCESS("새소식 조회에 성공했습니다."),
        SELECT_PROGRAM_SUCCESS("프로그램 조회에 성공했습니다."),
        EDIT_REGION_SUCCESS("지역 변경에 성공했습니다.");
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
