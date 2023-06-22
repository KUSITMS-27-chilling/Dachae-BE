package chilling.encore.service.oauth2;

import chilling.encore.service.oauth2.kakao.KakaoAuthService;
import chilling.encore.service.oauth2.kakao.KakaoService;
import chilling.encore.service.oauth2.naver.NaverAuthService;
import chilling.encore.service.oauth2.naver.NaverService;
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
