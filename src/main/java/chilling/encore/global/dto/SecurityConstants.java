package chilling.encore.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class SecurityConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SecurityExceptionList {
        REQUIRED_LOGGED_IN("403", HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");

        private final String errorCode;
        private final HttpStatus httpStatus;
        private final String message;
    }
}
