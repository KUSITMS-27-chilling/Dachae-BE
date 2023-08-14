package chilling.encore.domain.listenTogether.dto;

import lombok.*;

public abstract class ParticipantDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ParticipantRequest {
        private Long listenTogetherIdx;
    }
}
