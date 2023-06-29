package chilling.encore.global.config.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class JwtConstants {
    @Getter
    @RequiredArgsConstructor
    public enum JwtExcpetionMessage {
        INVALID_FORMAT("[ERROR] 토큰 형식이 올바르지 않습니다"),
        JWT_EXPIRED("[ERROR] 토큰이 만료되었습니다"),
        JWT_NOT_SUPPORTED("[ERROR] 지원되지 않는 토큰 입니다"),
        WRONG_TOKEN("[ERROR] 잘못된 토큰 입니다"),
        NON_AUTHORITY("[ERROR] 권한이 없습니다"),
        UNKHOWN_EXCEPTION("[ERROR] 알 수 없는 예외 발생!!"),
        REMOVED_ACCESS_TOKEN("[ERROR] 다른 컴퓨터에서 로그인 되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum JwtExcpetionCode {
        INVALID_FORMAT("jwt001"),
        JWT_EXPIRED("jwt002"),
        JWT_NOT_SUPPORTED("jwt003"),
        WRONG_TOKEN("jwt004"),
        UNKHOWN_EXCEPTION("jwt005"),
        NON_AUTHORITY("jwt006"),
        REMOVE_ACCESS_TOKEN("jwt007");
        private final String code;
    }

    @Getter
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
}
