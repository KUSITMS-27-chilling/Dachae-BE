package chilling.encore.dto;

import lombok.*;

public abstract class ParticipantDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ParticipantRequest {
        private Long listenTogetherIdx;
    }
}
