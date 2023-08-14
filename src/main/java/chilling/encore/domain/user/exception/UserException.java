package chilling.encore.domain.user.exception;

import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static chilling.encore.domain.user.constant.UserConstants.UserFailCode.NO_SUCH_IDX_CODE;
import static chilling.encore.domain.user.constant.UserConstants.UserFailMessage.NO_SUCH_IDX_MESSAGE;

public abstract class UserException extends ApplicationException {
    protected UserException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends UserException {

        public NoSuchIdxException() {
            super(NO_SUCH_IDX_MESSAGE.getMessage(), NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
