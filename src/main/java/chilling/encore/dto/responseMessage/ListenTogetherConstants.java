package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ListenTogetherConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherSuccessMessage {
        SELECT_SUCCESS_MESSAGE("같이들어요 조회에 성공했습니다."),
        CREATE_SUCCESS_MESSAGE("같이들어요 생성에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherFailMessage {
        SAVE_FAIL_MESSAGE("같이 들어요 생성에 실패했습니다.");
        private final String message;
    }
}
