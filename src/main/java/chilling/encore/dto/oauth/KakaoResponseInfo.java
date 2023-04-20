package chilling.encore.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoResponseInfo {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoAccount {
        private KakaoProfile profile;
        private Optional<String> email;
        private Optional<String> gender;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class KakaoProfile {
            private Optional<String> nickname;
        }
    }

    @JsonProperty("kakao_account")
    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }
}
