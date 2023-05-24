package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class RedisConstants {
    @Getter
    @RequiredArgsConstructor
    public enum RedisFailMessage {
        NO_SUCH_REFRESH_MESSAGE("[ERROR] 해당 토큰이 저장되어 있지 않습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum RedisFailCode {
        NO_SUCH_REFRESH_CODE("rd001");
        private final String code;
    }
}
