package chilling.encore.domain.listenTogether.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ParticipantConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        CREATE_SUCCESS_MESSAGE("신청에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum FailMessage {
        DUPLICATED_MESSAGE("[ERROR] 이미 신청한 게시글입니다."),
        MINE_MESSAGE("[ERROR] 본인이 작성한 게시글입니다."),
        FULL_MESSAGE("[ERROR] 신청 인원이 가득 찼습니다.");

        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum FailCode {
        DUPLICATED_CODE("pa001"),
        MINE_CODE("pa002"),
        FULL_CODE("pa003");

        private final String code;
    }
}