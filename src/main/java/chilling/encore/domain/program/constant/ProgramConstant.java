package chilling.encore.domain.program.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ProgramConstant {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        SELECT_NEW_SUCCESS("새소식 조회에 성공했습니다."),
        SELECT_PROGRAM_SUCCESS("프로그램 조회에 성공했습니다."),
        EDIT_REGION_SUCCESS("지역 변경에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProgramFailMessage {
        NO_SUCH_IDX_MESSAGE("[ERROR] 프로그램 번호가 잘못되었습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProgramFailCode {
        NO_SUCH_IDX_CODE("pr001");
        private final String code;
    }
}
