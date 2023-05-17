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

    @Getter
    @RequiredArgsConstructor
    public enum CenterFailMessage {
        NO_SUCH_REGION_MESSAGE("[ERROR] 센터 지역이 잘못되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CenterFailCode {
        NO_SUCH_REGION_CODE("ct001");
        private final String code;
    }
}
