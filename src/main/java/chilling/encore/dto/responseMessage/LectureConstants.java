package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LectureConstants {
    @Getter
    @RequiredArgsConstructor
    public enum LectureSuccessMessage {
        SELECT_SUCCESS_MESSAGE("우동강 조회에 성공했습니다"),
        SELECT_MY_MESSAGE("지원한 우동강 조회에 성공했습니다"),
        SELECT_TODAY_MESSAGE("오늘의 우동강 조회에 성공했습니다");

        private final String message;
    }
}
