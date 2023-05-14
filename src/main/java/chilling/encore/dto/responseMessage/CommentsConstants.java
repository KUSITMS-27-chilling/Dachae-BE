package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CommentsConstants {
    @Getter
    @RequiredArgsConstructor
    public enum CommentsSuccessMessage {
        SELECT_REVIEW_COMMENT_SUCCESS_MESSAGE("수강후기 댓글 조회에 성공했습니다"),
        SELECT_LISTEN_COMMENT_SUCCESS_MESSAGE("같이들어요 댓글 조회에 성공했습니다"),
        CREATE_REVIEW_COMMENT_SUCCESS_MESSAGE("수강후기 댓글 생성에 성공했습니다"),
        CREATE_LISTEN_COMMENT_SUCCESS_MESSAGE("같이들어요 댓글 생성에 성공했습니다");
        private final String message;
    }
}
