package chilling.encore.exception;

import chilling.encore.dto.responseMessage.ProgramConstant.ProgramFailCode;
import chilling.encore.dto.responseMessage.ProgramConstant.ProgramFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ProgramException extends ApplicationException {
    protected ProgramException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchIdxException extends ProgramException {
        public NoSuchIdxException() {
            super(ProgramFailMessage.NO_SUCH_IDX_MESSAGE.getMessage(), ProgramFailCode.NO_SUCH_IDX_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
