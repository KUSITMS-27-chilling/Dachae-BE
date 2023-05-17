package chilling.encore.exception;

import chilling.encore.dto.responseMessage.ReviewConstants;
import chilling.encore.dto.responseMessage.ReviewConstants.ReviewFailCode;
import chilling.encore.dto.responseMessage.ReviewConstants.ReviewFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ReviewException extends ApplicationException {
    protected ReviewException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends ReviewException{
        public NoSuchIdxException() {
            super(ReviewFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), ReviewFailCode.NO_SUCH_IDX_MESSAGE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
