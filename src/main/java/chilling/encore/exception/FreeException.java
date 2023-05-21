package chilling.encore.exception;

import chilling.encore.dto.responseMessage.FreeBoardConstants;
import chilling.encore.dto.responseMessage.FreeBoardConstants.FreeBoardFailCode;
import chilling.encore.dto.responseMessage.FreeBoardConstants.FreeBoardFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public abstract class FreeException extends ApplicationException {
    protected FreeException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends FreeException{
        public NoSuchIdxException() {
            super(FreeBoardFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), FreeBoardFailCode.NO_SUCH_IDX_CODE.getCode(), INTERNAL_SERVER_ERROR);
        }
    }
}