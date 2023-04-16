package chilling.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private String errorCode;
    private String message;

    public ErrorResponse(String errorCode, String message) {
        this.localDateTime = LocalDateTime.now().withNano(0);
        this.errorCode = errorCode;
        this.message = message;
    }
}
