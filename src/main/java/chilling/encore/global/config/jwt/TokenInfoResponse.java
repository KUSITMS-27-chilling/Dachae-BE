package chilling.encore.global.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenInfoResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpirationTime;

    public static TokenInfoResponse from(String grantType, String accessToken, String refreshToken, Long accessTokenExpirationTime) {
        return TokenInfoResponse.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpirationTime(accessTokenExpirationTime)
                .build();
    }
}