package chilling.encore.service.kakao;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto.UserLoginResponse;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.global.config.security.jwt.Oauth2PrincipalDetails;
import chilling.encore.dto.oauth.Oauth2SignUpResponse;
import chilling.encore.dto.oauth.KakaoResponseInfo;
import chilling.encore.repository.springDataJpa.UserRepository;
import chilling.encore.service.oauth2.Oauth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;
import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.LOGIN_SUCCESS;
import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.SIGNUP_CONTINUE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakaoService implements Oauth2Service {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseDto<?> authenticate(String provider, String accessToken) {
        log.info("accessToken = {}", accessToken);

        String email = null;
        String gender = null;

        ResponseEntity<KakaoResponseInfo> response = getKakaoResponseInfo(accessToken);
        // KAKAO API 호출 결과에서 필요한 데이터 추출
        KakaoResponseInfo profile = response.getBody();

        Long kakaoId = profile.getId();

        Optional<String> optionalEmail = profile.getKakaoAccount().getEmail();
        if (optionalEmail.isPresent())
            email = optionalEmail.get();
        Optional<String> optionalGender = profile.getKakaoAccount().getGender();
        if (optionalGender.isPresent())
            gender = optionalGender.get();
        return getResponseDto(email, gender, kakaoId, provider);
    }

    private ResponseDto<?> getResponseDto(String email, String gender, Long kakaoId, String provider) {
        // 회원 가입 여부 확인
        Optional<User> optionalUser = userRepository.findByUserId(String.valueOf(kakaoId));
        if (optionalUser.isEmpty()) {
            // 회원이 아닌 경우 추가 정보를 받음
            return ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SIGNUP_CONTINUE.getMessage(), Oauth2SignUpResponse.builder()
                    .id(String.valueOf(kakaoId))
                    .email(email)
                    .gender(gender)
                    .provider(provider)
                    .build()
            );
        }
        return getExistingUser(optionalUser);
    }

    private ResponseDto<UserLoginResponse> getExistingUser(Optional<User> optionalUser) {
        User user = optionalUser.get();
        // 회원인 경우 jwt 토큰 발행

        Oauth2PrincipalDetails principalDetails = new Oauth2PrincipalDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        TokenInfoResponse token = tokenProvider.oauth2CreateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserLoginResponse loginResponse = UserLoginResponse.from(token);
        return ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), LOGIN_SUCCESS.getMessage(), loginResponse);
    }

    private ResponseEntity<KakaoResponseInfo> getKakaoResponseInfo(String accessToken) {
        // KAKAO API 호출을 위한 RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // KAKAO API 호출을 위한 HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // KAKAO API 호출을 위한 HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // KAKAO API 호출
        ResponseEntity<KakaoResponseInfo> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoResponseInfo.class);
        return response;
    }
}
