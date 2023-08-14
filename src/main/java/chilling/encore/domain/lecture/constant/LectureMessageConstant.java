package chilling.encore.domain.lecture.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LectureMessageConstant {
    @Getter
    @RequiredArgsConstructor
    public enum LectureMessageSuccessMessage {
        CREATE_SUCCESS_MESSAGE("강사에게 메세지 전송에 성공했습니다.");

        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum LectureMessageFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 게시글 번호가 잘못되었습니다."),
        SEND_FAIL_MESSAGE("[ERROR] 강사에메 메시지 전송에 실패했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum LectureMessageFailCode {
        NO_SUCH_IDX_CODE("lm001"),
        SEND_FAIL_CODE("lm002");
        private final String code;
    }
}
