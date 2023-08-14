package chilling.encore.domain.listenTogether.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ListenTogetherConstants {
    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherSuccessMessage {
        SELECT_SUCCESS_MESSAGE("같이들어요 조회에 성공했습니다."),
        SELECT_POPULAR_SUCCESS_MESSAGE("같이들어요 인기글 조회에 성공했습니다."),
        SELECT_MINE_SUCCESS_MESSAGE("내가 제안한 글 조회에 성공했습니다."),
        SELECT_DETAIL_SUCCESS_MESSAGE("같이 들어요 게시글 상세 조회에 성공했습니다."),
        CREATE_SUCCESS_MESSAGE("같이들어요 생성에 성공했습니다.");

        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 게시글 번호가 잘못되었습니다."),
        SAVE_FAIL_MESSAGE("[ERROR] 같이 들어요 생성에 실패했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ListenTogetherFailCode {
        NO_SUCH_IDX_CODE("li001");
        private final String code;
    }
}
