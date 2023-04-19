package chilling.encore.service;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.dto.UserDto.UserLoginRequest;
import chilling.encore.dto.UserDto.UserLoginResponse;
import chilling.encore.dto.UserDto.UserSignUpRequest;
import chilling.encore.dto.responseMessage.UserConstants;
import chilling.encore.global.config.jwt.JwtTokenProvider;
import chilling.encore.global.config.jwt.TokenInfoResponse;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static chilling.encore.dto.responseMessage.UserConstants.Role.ROLE_USER;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisRepository redisRepository;

    //회원가입
    public User signUp(UserSignUpRequest userSignUpRequest) {
        User user = User.builder()
                .userId(userSignUpRequest.getId())
                .name(userSignUpRequest.getName())
                .gender(userSignUpRequest.getGender())
                .age(userSignUpRequest.getAge())
                .email(userSignUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignUpRequest.getPassword()))
                .nickName(userSignUpRequest.getNickName())
                .phoneNumber(userSignUpRequest.getPhoneNumber())
                .role(ROLE_USER)
                .status(0)
                .build();
        return userRepository.save(user);
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        try {
            TokenInfoResponse tokenInfoResponse = validateLogin(userLoginRequest);
            return UserLoginResponse.from(tokenInfoResponse);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    private TokenInfoResponse validateLogin(UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginRequest.getId(), userLoginRequest.getPassword());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfoResponse tokenInfoResponse = jwtTokenProvider.createToken(authentication);
        return tokenInfoResponse;
    }

    public boolean checkIdDup(String id) {
        Optional<User> byUserId = userRepository.findByUserId(id);
        if (byUserId.isPresent())
            return false;
        return true;
    }

    public boolean checkNick(String nickName) {
        Optional<User> byNickName = userRepository.findByNickName(nickName);
        if (byNickName.isPresent())
            return false;
        return true;
    }

    public User validateUserId(String id) {
        User user = userRepository.findByUserId(id).orElseThrow();
        return user;
    }
}
