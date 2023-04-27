package chilling.encore.controller;


import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.dto.UserDto.Oauth2SignUpRequest;
import chilling.encore.dto.UserDto.UserLoginRequest;
import chilling.encore.dto.UserDto.UserLoginResponse;
import chilling.encore.dto.UserDto.UserSignUpRequest;
import chilling.encore.dto.responseMessage.UserConstants;
import chilling.encore.global.config.email.EmailService;
import chilling.encore.service.UserService;
import chilling.encore.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.*;
import static chilling.encore.dto.responseMessage.UserConstants.UserExceptionList.NOT_FOUND_USER;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "USER API")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    //회원가입 Controller
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 완료합니다.")
    public ResponseEntity<ResponseDto<User>> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        userService.signUp(userSignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/signup/oauth2")
    @ApiOperation(value = "oauth2 회원 가입", notes = "oauth2 추가 정보 받은 이후 회원가입 진행")
    public ResponseEntity<ResponseDto> signUp(@RequestBody Oauth2SignUpRequest oauth2SignUpRequest) {
        User user = userService.oauth2signUp(oauth2SignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/checkIdDup")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkId(String id) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        CHECK_DUP.getMessage(),
                        userService.checkIdDup(id)
                )
        );
    }

    @PostMapping("/checkNickDup")
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkNick(String nickName) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        CHECK_DUP.getMessage(),
                        userService.checkNick(nickName)
                )
        );
    }

    @PostMapping("/sendMail")
    @ApiOperation(value = "이메일 인증 전송", notes = "이메일 인증을 전송합니다.")
    public ResponseEntity<ResponseDto<String>> sendMail(String email) {
        try {
            log.info("email = {}", email);
            String s = emailService.sendSimpleMessage(email);
            return ResponseEntity.ok(ResponseDto.create(EMAIL_SEND_SUCCESS.getMessage(), s));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 진행")
    public ResponseEntity<ResponseDto<UserLoginResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse loginResponse = userService.login(userLoginRequest);
            return ResponseEntity.ok(ResponseDto.create(LOGIN_SUCCESS.getMessage(), loginResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.ok(ResponseDto.create(NOT_FOUND_USER.getMessage()));
        }
    }
}
