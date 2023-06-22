package chilling.encore.service.oauth2;

public interface Oauth2AuthService {
    String getAccessToken(String code, String state);
}
