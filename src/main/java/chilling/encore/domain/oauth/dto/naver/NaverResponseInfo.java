package chilling.encore.domain.oauth.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverResponseInfo {
    private NaverResponse naverResponse;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NaverResponse {
        private String id;
        private Optional<String> email;
        private Optional<String> gender;
    }

    @JsonProperty("response")
    public void setNaverResponse(NaverResponse naverResponse) {
        this.naverResponse = naverResponse;
    }
}
