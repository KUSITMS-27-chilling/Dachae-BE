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

    private Long userIdx;

    public static TokenInfoResponse from(String grantType, String accessToken, String refreshToken, Long accessTokenExpirationTime, Long userIdx) {
        return TokenInfoResponse.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpirationTime(accessTokenExpirationTime)
                .userIdx(userIdx)
                .build();
    }
}