package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ParticipantConstants {
    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        CREATE_SUCCESS_MESSAGE("신청에 성공했습니다.");
        private final String message;
    }
}