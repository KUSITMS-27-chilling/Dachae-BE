package chilling.encore.service.oauth2;

import chilling.encore.global.dto.ResponseDto;

public interface Oauth2Service {
    ResponseDto<?> authenticate(String provider, String accessToken);
}
