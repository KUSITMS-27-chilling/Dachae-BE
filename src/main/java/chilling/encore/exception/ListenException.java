package chilling.encore.exception;

import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static chilling.encore.dto.responseMessage.ListenTogetherConstants.ListenTogetherFailCode.NO_SUCH_IDX_CODE;
import static chilling.encore.dto.responseMessage.ListenTogetherConstants.ListenTogetherFailMessage.NO_SUCH_IDX_MESSAGE;

public abstract class ListenException extends ApplicationException {
    protected ListenException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends ListenException {
        public NoSuchIdxException() {
            super(NO_SUCH_IDX_MESSAGE.getMessage(), NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
