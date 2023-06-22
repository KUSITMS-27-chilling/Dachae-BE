package chilling.encore.service.oauth2.naver;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.dto.oauth.KakaoResponseInfo;
import chilling.encore.dto.oauth.NaverResponseInfo;
import chilling.encore.dto.oauth.Oauth2SignUpResponse;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import chilling.encore.global.config.security.jwt.Oauth2PrincipalDetails;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.repository.springDataJpa.UserRepository;
import chilling.encore.service.oauth2.Oauth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Optional;

import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.LOGIN_SUCCESS;
import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.SIGNUP_CONTINUE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NaverService implements Oauth2Service {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseDto<?> authenticate(String provider, String accessToken) {
        log.info("accessToken = {}", accessToken);

        String email = null;
        String gender = null;

        ResponseEntity<NaverResponseInfo> response = getNaverResponseInfo(accessToken);
        // Naver API 호출 결과에서 필요한 데이터 추출
        NaverResponseInfo profile = response.getBody();

        String naverId = profile.getNaverResponse().getId();

        Optional<String> optionalEmail = profile.getNaverResponse().getEmail();
        if (optionalEmail.isPresent())
            email = optionalEmail.get();
        Optional<String> optionalGender = profile.getNaverResponse().getGender();
        if (optionalGender.isPresent()) {
            switch (optionalGender.get()) {
                case "M" :
                    gender = "male";
                    break;
                case "F" :
                    gender = "female";
                    break;
            }
        }
        return getResponseDto(email, gender, naverId, provider);
    }

    private ResponseDto<?> getResponseDto(String email, String gender, String naverId, String provider) {
        // 회원 가입 여부 확인
        Optional<User> optionalUser = userRepository.findByUserId(String.valueOf(naverId));
        if (optionalUser.isEmpty()) {
            // 회원이 아닌 경우 추가 정보를 받음
            return ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SIGNUP_CONTINUE.getMessage(), Oauth2SignUpResponse.builder()
                    .id(String.valueOf(naverId))
                    .email(email)
                    .gender(gender)
                    .provider(provider)
                    .build()
            );
        }
        return getExistingUser(optionalUser);
    }

    private ResponseDto<UserDto.UserLoginResponse> getExistingUser(Optional<User> optionalUser) {
        User user = optionalUser.get();
        // 회원인 경우 jwt 토큰 발행

        Oauth2PrincipalDetails principalDetails = new Oauth2PrincipalDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        TokenInfoResponse token = tokenProvider.oauth2CreateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDto.UserLoginResponse loginResponse = UserDto.UserLoginResponse.from(token);
        return ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), LOGIN_SUCCESS.getMessage(), loginResponse);
    }

    private ResponseEntity<NaverResponseInfo> getNaverResponseInfo(String accessToken) {
        // Naver API 호출을 위한 RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // Naver API 호출을 위한 HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Naver API 호출을 위한 HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Naver API 호출
        ResponseEntity<NaverResponseInfo> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                NaverResponseInfo.class);
        return response;
    }
}
