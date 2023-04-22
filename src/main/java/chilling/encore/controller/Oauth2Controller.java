package chilling.encore.controller;

import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {
    private final SelectOauthService selectOauthService;

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<ResponseDto<?>> getUser(@PathVariable String provider, String code) {
        provider = provider.toUpperCase();
        Oauth2Service oauth2Service = selectOauthService.selectService(provider);
        Oauth2AuthService oauth2AuthService = selectOauthService.selectAuth(provider);

        String accessToken = oauth2AuthService.getAccessToken(code);
        ResponseDto<?> authenticate = oauth2Service.authenticate(accessToken);

        return ResponseEntity.ok(authenticate);
    }

    @GetMapping("/login/oauth2/{provider}")
    public ResponseEntity<ResponseDto<?>> loginOAuth2Code(@PathVariable String provider, String token) {
        Oauth2Service oauth2Service = selectOauthService.selectService(provider);
        ResponseDto<?> authenticate = oauth2Service.authenticate(token);
        return ResponseEntity.ok(authenticate);
    }
}
