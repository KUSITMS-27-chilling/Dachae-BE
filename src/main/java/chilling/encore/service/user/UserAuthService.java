package chilling.encore.service.user;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.exception.RedisException;
import chilling.encore.exception.UserException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserAuthService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisRepository redisRepository;
    private final JwtTokenProvider tokenProvider;
    private SecurityUtils securityUtils = new SecurityUtils();

    public UserDto.UserLoginResponse login(UserDto.UserLoginRequest userLoginRequest) {
        try {
            TokenInfoResponse tokenInfoResponse = validateLogin(userLoginRequest);

            String id = userLoginRequest.getId();
            User user = userRepository.findByUserId(id).orElseThrow(() -> new UserException.NoSuchIdxException());
            LocalDate now = LocalDate.now();
            user.updateLoginAt(now);

            return UserDto.UserLoginResponse.from(tokenInfoResponse);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    private TokenInfoResponse validateLogin(UserDto.UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.getId(), userLoginRequest.getPassword());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(authentication);
        return tokenInfoResponse;
    }

    public void logout(String authorization) {
        String token = authorization.substring(7);
        String userIdx = tokenProvider.getUserIdx(token);
        try {
            Long expiration = tokenProvider.getExpiration(token);
            redisRepository.setValues("blackList:" + token, token, Duration.ofSeconds(expiration)); //Access Token 남은 시간동안 블랙리스트
        } catch (ExpiredJwtException e) {
        } finally {
            redisRepository.deleteValues(String.valueOf(userIdx));
        }
    }

    public UserDto.UserLoginResponse reIssueToken() {
        Long userIdx = securityUtils.getLoggedInUser()
                .orElseThrow(() -> new UserException.NoSuchIdxException()).getUserIdx();
        String refreshToken = redisRepository.getValues(userIdx.toString()).orElseThrow(() -> new RedisException.NoSuchRefreshToken());
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(authentication);
        log.info("재발급 & 저장!");
        return UserDto.UserLoginResponse.from(tokenInfoResponse);
    }
}
