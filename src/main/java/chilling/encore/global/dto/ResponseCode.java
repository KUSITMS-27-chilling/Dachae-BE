package chilling.encore.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class ResponseCode {

    @Getter
    @RequiredArgsConstructor
    public enum globalSuccessCode {
        SELECT_SUCCESS_CODE(200),
        CREATE_SUCCESS_CODE(201),
        DELETE_SUCCESS_CODE(204);
        private final int code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum globalFailCode {
        AUTHORIZATION_FAIL_CODE(403),
        BAD_REQUEST(400),
        SERVER_ERROR(500);
        private final int code;
    }


}
