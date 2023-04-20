package chilling.encore.service.kakao;

import chilling.encore.service.Oauth2AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@Getter
public class KakaoAuthService implements Oauth2AuthService {
    @Value("${oauth2.kakao.client-id}")
    private String clientId;
    @Value("${oauth2.kakao.client-secret}")
    private String clientSecret;
    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${oauth2.kakao.authorization-grant-type}")
    private String authorizationGrantType;

    public String getAccessToken(String code) {
        log.info("code = {}", code);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", authorizationGrantType);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);
        log.info("requestBody = {}", requestBody);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, entity, JsonNode.class);
        log.info("response = {}", response);

        String accessToken = response.getBody().get("access_token").asText();

        log.info("accessToken = {}", accessToken);
        return accessToken;
    }
}
