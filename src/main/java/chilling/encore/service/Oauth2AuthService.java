package chilling.encore.service;

public interface Oauth2AuthService {
    String getAccessToken(String code);
}
