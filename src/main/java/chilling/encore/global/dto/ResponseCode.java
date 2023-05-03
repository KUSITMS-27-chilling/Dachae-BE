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
}
