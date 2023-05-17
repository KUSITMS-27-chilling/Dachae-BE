package chilling.encore.exception;

import chilling.encore.dto.responseMessage.CenterConstants;
import chilling.encore.dto.responseMessage.CenterConstants.CenterFailCode;
import chilling.encore.dto.responseMessage.CenterConstants.CenterFailMessage;
import chilling.encore.global.exception.ApplicationException;
import chilling.encore.repository.springDataJpa.CenterRepository;
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
