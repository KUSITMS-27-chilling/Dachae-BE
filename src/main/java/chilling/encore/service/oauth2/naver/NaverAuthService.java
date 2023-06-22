package chilling.encore.service.oauth2.naver;

import chilling.encore.service.oauth2.Oauth2AuthService;
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
public class NaverAuthService implements Oauth2AuthService {
    @Value("${oauth2.naver.client-id}")
    private String clientId;
    @Value("${oauth2.naver.client-secret}")
    private String clientSecret;
    @Value("${oauth2.naver.redirect-uri}")
    private String redirectUri;
    @Value("${oauth2.naver.authorization-grant-type}")
    private String authorizationGrantType;

    public String getAccessToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = getRequestBody(code, state);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, entity, JsonNode.class);

        String accessToken = response.getBody().get("access_token").asText();
        return accessToken;
    }

    private MultiValueMap<String, String> getRequestBody(String code, String state) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", authorizationGrantType);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", code);
        requestBody.add("state", state);
        return requestBody;
    }
}
