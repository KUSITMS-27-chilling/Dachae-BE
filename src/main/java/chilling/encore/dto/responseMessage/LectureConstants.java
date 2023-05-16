package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LectureConstants {
    @Getter
    @RequiredArgsConstructor
    public enum LectureCategory {
        CULTURAL_LEISURE("문화여가"),
        SELF_DEVELOPMENT("자기개발"),
        JOB_ABILITY("직업능력"),
        CITIZEN_PARTICIPATION("시민참여"),
        HUMANITIES("인문교양"),
        ETC("기타");
        private final String category;
    }
    @Getter
    @RequiredArgsConstructor
    public enum LectureSuccessMessage {
        SELECT_SUCCESS_MESSAGE("우동강 조회에 성공했습니다"),
        SELECT_MY_MESSAGE("지원한 우동강 조회에 성공했습니다"),
        SELECT_TODAY_MESSAGE("오늘의 우동강 조회에 성공했습니다");

        private final String message;
    }
}
