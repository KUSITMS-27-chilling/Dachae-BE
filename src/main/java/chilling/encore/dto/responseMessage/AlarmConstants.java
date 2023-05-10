package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AlarmConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        SELECT_ALARM_SUCCESS("알람 확인 조회에 성공했습니다.");
        private final String message;
    }
}
