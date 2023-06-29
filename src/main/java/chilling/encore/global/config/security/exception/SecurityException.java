package chilling.encore.global.config.security.exception;

import chilling.encore.global.config.security.jwt.JwtConstants;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage;
import chilling.encore.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode.*;
import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage.UNKHOWN_EXCEPTION;

public abstract class SecurityException extends ApplicationException {
    protected SecurityException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

    public static class RemovedAccessTokenException extends SecurityException {
        public RemovedAccessTokenException() {
            super(JwtExcpetionMessage.REMOVED_ACCESS_TOKEN.getMessage(), REMOVE_ACCESS_TOKEN.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class InvalidJwtFormatException extends SecurityException {
        public InvalidJwtFormatException() {
            super(JwtExcpetionMessage.INVALID_FORMAT.getMessage(), INVALID_FORMAT.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class ExpiredJwtException extends SecurityException {
        public ExpiredJwtException() {
            super(JwtExcpetionMessage.JWT_EXPIRED.getMessage(), JWT_EXPIRED.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class NonSupportedJwtException extends SecurityException {
        public NonSupportedJwtException() {
            super(JwtExcpetionMessage.JWT_NOT_SUPPORTED.getMessage(), JWT_NOT_SUPPORTED.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class WrongTokenException extends SecurityException {
        public WrongTokenException() {
            super(JwtExcpetionMessage.WRONG_TOKEN.getMessage(), WRONG_TOKEN.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class UnKnownException extends SecurityException {
        public UnKnownException() {
            super(JwtExcpetionMessage.UNKHOWN_EXCEPTION.getMessage(), JwtExcpetionCode.UNKHOWN_EXCEPTION.getCode(), HttpStatus.FORBIDDEN);
        }
    }

    public static class AuthorityException extends SecurityException {
        public AuthorityException() {
            super(JwtExcpetionMessage.NON_AUTHORITY.getMessage(), NON_AUTHORITY.getCode(), HttpStatus.FORBIDDEN);
        }
    }
}
