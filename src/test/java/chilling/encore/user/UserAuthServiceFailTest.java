package chilling.encore.user;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.exception.RedisException;
import chilling.encore.exception.UserException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.service.user.UserAuthService;
import chilling.encore.utils.domain.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserAuthServiceFailTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RedisRepository redisRepository;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private UserAuthService userAuthService;

    private UserDto.UserLoginRequest userLoginRequest;
    private User user = new MockUser();

    @BeforeEach
    void setUp() {
        userLoginRequest = new UserDto.UserLoginRequest(user.getUserId(), user.getPassword());
    }

    @Test
    void loginFailTest() {
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(new AuthenticationException("Failed to authenticate") {});

        assertThatThrownBy(() -> userAuthService.login(userLoginRequest)).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void reIssueUserIdxFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.empty());
        assertThatThrownBy(() -> userAuthService.reIssueToken()).isInstanceOf(UserException.NoSuchIdxException.class);
    }
    @Test
    void reIssueInvalidTokenFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));
        given(redisRepository.getValues(user.getUserIdx().toString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userAuthService.reIssueToken())
                .isInstanceOf(RedisException.NoSuchRefreshToken.class);
    }
}
