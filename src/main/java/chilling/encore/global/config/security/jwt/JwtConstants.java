package chilling.encore.global.config.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class JwtConstants {
    @Getter
    @RequiredArgsConstructor
    public enum JwtExcpetionMessage {
        INVALID_FORMAT("토큰 형식이 올바르지 않습니다"),
        JWT_EXPIRED("토큰이 만료되었습니다"),
        JWT_NOT_SUPPORTED("지원되지 않는 토큰 입니다"),
        WRONG_TOKEN("잘못된 토큰 입니다"),
        NON_AUTHORITY("권한이 없습니다"),
        UNKHOWN_EXCEPTION("알 수 없는 예외 발생!!");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum JwtExcpetionCode {
        INVALID_FORMAT("jwt01"),
        JWT_EXPIRED("jwt02"),
        JWT_NOT_SUPPORTED("jwt03"),
        WRONG_TOKEN("jwt04"),
        UNKHOWN_EXCEPTION("jwt05"),
        NON_AUTHORITY("jwt06");
        private final String code;
    }

    @Getter
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
}
