package chilling.encore.service;

import chilling.encore.global.dto.ResponseDto;

public interface Oauth2Service {
    ResponseDto<?> authenticate(String accessToken);
}
