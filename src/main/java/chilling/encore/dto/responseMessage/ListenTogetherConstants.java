package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ListenTogetherConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherSuccessMessage {
        LISTEN_TOGETHER_SUCCESS_MESSAGE("같이들어요 조회에 성공했습니다.");
        private final String message;
    }
}
