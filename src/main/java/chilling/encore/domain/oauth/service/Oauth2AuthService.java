package chilling.encore.domain.oauth.service;

public interface Oauth2AuthService {
    String getAccessToken(String code, String state);
}
