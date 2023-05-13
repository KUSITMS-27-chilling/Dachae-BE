package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CommentsConstants {
    @Getter
    @RequiredArgsConstructor
    public enum CommentsSuccessMessage {
        CREATE_SUCCESS_MESSAGE("댓글 생성에 성공했습니다");
        private final String message;
    }
}
