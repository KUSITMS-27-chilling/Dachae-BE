package chilling.encore.user;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.exception.RedisException.NoSuchRefreshToken;
import chilling.encore.exception.UserException.NoSuchIdxException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import chilling.encore.global.config.security.jwt.PrincipalDetails;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.UserRepository;
import chilling.encore.service.user.UserAuthService;
import chilling.encore.utils.MockUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserAuthTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisRepository redisRepository;
    @InjectMocks
    private UserAuthService userAuthService;

    private UserDto.UserLoginRequest userLoginRequest;
    private SecurityUtils securityUtils;
    private User user = new MockUser().getUser();

    @BeforeEach
    public void setUp() {
        userLoginRequest = new UserDto.UserLoginRequest(user.getUserId(), user.getPassword());
        securityUtils = new SecurityUtils();

        // Mock 사용자 정보를 SecurityContextHolder에 설정 (securityContextHolder에서 꺼내서 검증)
        SecurityContext securityContext = SecurityContextHolder.getContext();
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        securityContext.setAuthentication(authentication);
    }

    @Test
    public void loginTest() {
        given(userRepository.findByUserId(user.getUserId())).willReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);

        TokenInfoResponse tokenInfoResponse = new TokenInfoResponse("Bearer", "access_token", "refresh_token", 3000L);
        given(jwtTokenProvider.createToken(any(Authentication.class))).willReturn(tokenInfoResponse);

        UserDto.UserLoginResponse loginResponse = userAuthService.login(userLoginRequest);

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getAccessToken()).isEqualTo(tokenInfoResponse.getAccessToken());
        assertThat(loginResponse.getRefreshToken()).isEqualTo(tokenInfoResponse.getRefreshToken());
    }

    @Test
    public void loginFailTest() {
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(new AuthenticationException("Failed to authenticate") {});

        assertThatThrownBy(() -> userAuthService.login(userLoginRequest)).isInstanceOf(AuthenticationException.class);
    }

    @Test
    public void logoutTest() {
        String authorization = "Bearer access_token";
        String token = "access_token";
        String userIdx = "userIdx";
        given(jwtTokenProvider.getUserIdx(token)).willReturn(userIdx);
        given(jwtTokenProvider.getExpiration(token)).willReturn(3600L);

        userAuthService.logout(authorization);

        then(redisRepository).should().setValues("blackList:" + token, token, Duration.ofSeconds(3600L));
        then(redisRepository).should().deleteValues(userIdx);
    }

    @Test
    public void reIssueTokenTest() {
        String refreshToken = "refresh_token";
        given(redisRepository.getValues(user.getUserIdx().toString())).willReturn(Optional.of(refreshToken));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
        given(jwtTokenProvider.getAuthentication(refreshToken)).willReturn(authentication);

        TokenInfoResponse tokenInfoResponse = new TokenInfoResponse("Bearer", "new_access_token", "new_refresh_token", 3600L);
        given(jwtTokenProvider.createToken(authentication)).willReturn(tokenInfoResponse);

        UserDto.UserLoginResponse reIssueResponse = userAuthService.reIssueToken();

        assertThat(reIssueResponse).isNotNull();
        assertThat(reIssueResponse.getAccessToken())
                .isEqualTo(tokenInfoResponse.getAccessToken());
        assertThat(reIssueResponse.getRefreshToken())
                .isEqualTo(tokenInfoResponse.getRefreshToken());
    }

    @Test
    public void reIssueFailTest() {
        given(redisRepository.getValues(user.getUserIdx().toString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userAuthService.reIssueToken())
                .isInstanceOf(NoSuchRefreshToken.class);
    }
}