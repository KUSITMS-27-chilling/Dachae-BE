package chilling.encore.domain.oauth.service;

import chilling.encore.domain.oauth.service.kakao.KakaoAuthService;
import chilling.encore.domain.oauth.service.kakao.KakaoService;
import chilling.encore.domain.oauth.service.naver.NaverAuthService;
import chilling.encore.domain.oauth.service.naver.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SelectOauthService {
    private final KakaoAuthService kakaoAuthService;
    private final NaverAuthService naverAuthService;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    public Oauth2Service selectService(String provider) {
        switch (provider) {
            case "KAKAO" : return kakaoService;
            case "NAVER" : return naverService;
        }
        return null;
    }

    public Oauth2AuthService selectAuth(String provider) {
        switch (provider) {
            case "KAKAO" : return kakaoAuthService;
            case "NAVER" : return naverAuthService;
        }
        return null;
    }
}
