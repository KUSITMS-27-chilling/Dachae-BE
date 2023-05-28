package chilling.encore.exception;

import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static chilling.encore.dto.responseMessage.RedisConstants.RedisFailCode.NO_SUCH_REFRESH_CODE;
import static chilling.encore.dto.responseMessage.RedisConstants.RedisFailMessage.NO_SUCH_REFRESH_MESSAGE;

public abstract class RedisException extends ApplicationException {
    protected RedisException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchRefreshToken extends RedisException {
        public NoSuchRefreshToken() {
            super(NO_SUCH_REFRESH_MESSAGE.getMessage(), NO_SUCH_REFRESH_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
