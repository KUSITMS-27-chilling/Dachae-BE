package chilling.encore.domain.lecture.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LectureConstants {
    @Getter
    @RequiredArgsConstructor
    public enum LectureSuccessMessage {
        SELECT_SUCCESS_MESSAGE("우동강 조회에 성공했습니다"),
        SELECT_MY_MESSAGE("지원한 우동강 조회에 성공했습니다"),
        SELECT_TODAY_MESSAGE("오늘의 우동강 조회에 성공했습니다"),
        SELECT_DETAIL_IMAGES("상세보기 이미지 조회에 성공했습니다"),
        SELECT_DETAIL_TEACHER("상세보기 강사 정보 조회에 성공했습니다"),
        SELECT_DETAIL_BASIC_INFO("상세보기 수업 기본정보 조회에 성공했습니다");

        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum LectureFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 동네 강의 번호가 잘못되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum LectureFailCode {
        NO_SUCH_IDX_CODE("lc001");
        private final String code;
    }
}
