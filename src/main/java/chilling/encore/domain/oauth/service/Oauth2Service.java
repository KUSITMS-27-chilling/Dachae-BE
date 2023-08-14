package chilling.encore.domain.oauth.service;

import chilling.encore.global.dto.ResponseDto;

public interface Oauth2Service {
    ResponseDto<?> authenticate(String provider, String accessToken);
}
