package chilling.encore.domain.lecture.exception;

import chilling.encore.domain.lecture.constant.LectureConstants.LectureFailCode;
import chilling.encore.domain.lecture.constant.LectureConstants.LectureFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class LectureException extends ApplicationException {
    protected LectureException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends LectureException {
        public NoSuchIdxException() {
            super(LectureFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), LectureFailCode.NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
