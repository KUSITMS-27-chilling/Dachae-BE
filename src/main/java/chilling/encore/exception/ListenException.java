package chilling.encore.exception;

import chilling.encore.dto.responseMessage.ListenTogetherConstants;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ListenException extends ApplicationException {
    protected ListenException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends ListenException {
        public NoSuchIdxException() {
            super(ListenTogetherConstants.ListenTogetherFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), ListenTogetherConstants.ListenTogetherFailCode.NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
