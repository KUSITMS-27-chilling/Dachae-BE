package chilling.encore.domain.listenTogether.exception;

import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static chilling.encore.domain.listenTogether.constant.ParticipantConstants.FailCode.*;
import static chilling.encore.domain.listenTogether.constant.ParticipantConstants.FailMessage.*;

public abstract class ParticipantException extends ApplicationException {
    protected ParticipantException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class DuplicateParticipationException extends ParticipantException{
        public DuplicateParticipationException() {
            super(DUPLICATED_MESSAGE.getMessage(), DUPLICATED_CODE.getCode(), HttpStatus.BAD_REQUEST);
        }
    }

    public static class MyListenTogetherException extends ParticipantException{
        public MyListenTogetherException() {
            super(MINE_MESSAGE.getMessage(), MINE_CODE.getCode(), HttpStatus.BAD_REQUEST);
        }
    }

    public static class FullParticipantException extends ParticipantException{
        public FullParticipantException() {
            super(FULL_MESSAGE.getMessage(), FULL_CODE.getCode(), HttpStatus.BAD_REQUEST);
        }
    }
}
