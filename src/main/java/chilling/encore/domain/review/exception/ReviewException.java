package chilling.encore.domain.review.exception;

import chilling.encore.domain.review.constant.ReviewConstants.ReviewFailCode;
import chilling.encore.domain.review.constant.ReviewConstants.ReviewFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ReviewException extends ApplicationException {
    protected ReviewException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends ReviewException{
        public NoSuchIdxException() {
            super(ReviewFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), ReviewFailCode.NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
