package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CenterConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        SELECT_INFO_SUCCESS("센터 정보 조회에 성공했습니다.");
        private final String message;
    }
}
