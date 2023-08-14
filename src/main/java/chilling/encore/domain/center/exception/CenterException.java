package chilling.encore.domain.center.exception;

import chilling.encore.domain.center.constant.CenterConstants.CenterFailCode;
import chilling.encore.domain.center.constant.CenterConstants.CenterFailMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class CenterException extends ApplicationException {
    protected CenterException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class NoSuchRegionException extends CenterException {

        public NoSuchRegionException() {
            super(CenterFailMessage.NO_SUCH_REGION_MESSAGE.getMessage(), CenterFailCode.NO_SUCH_REGION_CODE.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
