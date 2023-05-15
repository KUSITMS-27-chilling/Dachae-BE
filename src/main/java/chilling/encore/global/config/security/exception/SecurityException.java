package chilling.encore.global.config.security.exception;

import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class SecurityException extends ApplicationException {
    protected SecurityException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class InvalidJwtFormatException extends SecurityException {
        public InvalidJwtFormatException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    public static class ExpiredJwtException extends SecurityException {
        public ExpiredJwtException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    public static class NonSupportedJwtException extends SecurityException {
        public NonSupportedJwtException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    public static class WrongTokenException extends SecurityException {
        public WrongTokenException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    public static class UnKnownException extends SecurityException {
        public UnKnownException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }

    public static class AuthorityException extends SecurityException {
        public AuthorityException(String message, String errorCode, HttpStatus httpStatus) {
            super(message, errorCode, httpStatus);
        }
    }
}
