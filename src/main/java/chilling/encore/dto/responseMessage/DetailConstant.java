package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DetailConstant {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        DETAIL_SELECT_SUCCESS("상세페이지 조회에 성공했습니다.");
        private final String message;
    }
}
