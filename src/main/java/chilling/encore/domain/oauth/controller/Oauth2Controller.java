package chilling.encore.domain.oauth.controller;

import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.oauth.service.Oauth2AuthService;
import chilling.encore.domain.oauth.service.Oauth2Service;
import chilling.encore.domain.oauth.service.SelectOauthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {
    private final SelectOauthService selectOauthService;

    @ApiOperation(value = "이건 맵핑X", notes = "자동으로 처리되는 부분")
    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<ResponseDto<?>> getUser(@PathVariable String provider, String code, @Nullable String state) {
        provider = provider.toUpperCase();
        Oauth2Service oauth2Service = selectOauthService.selectService(provider);
        Oauth2AuthService oauth2AuthService = selectOauthService.selectAuth(provider);

        String accessToken = oauth2AuthService.getAccessToken(code, state);
        ResponseDto<?> authenticate = oauth2Service.authenticate(provider, accessToken);

        return ResponseEntity.ok(authenticate);
    }

}
