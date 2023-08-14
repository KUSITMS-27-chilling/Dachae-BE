package chilling.encore.domain.user.controller;

import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.user.dto.UserDto;
import chilling.encore.global.config.email.EmailService;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.user.service.UserSingUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.domain.user.constant.UserConstants.SuccessMessage.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "USER_SIGNUP API")
public class UserSignUpController {
    private final UserSingUpService userSingUpService;
    private final EmailService emailService;

    //회원가입 Controller
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 완료합니다.")
    public ResponseEntity<ResponseDto<User>> signUp(@RequestBody UserDto.UserSignUpRequest userSignUpRequest) {
        userSingUpService.signUp(userSignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/signup/oauth2")
    @ApiOperation(value = "oauth2 회원 가입", notes = "oauth2 추가 정보 받은 이후 회원가입 진행")
    public ResponseEntity<ResponseDto> signUp(@RequestBody UserDto.Oauth2SignUpRequest oauth2SignUpRequest) {
        userSingUpService.oauth2signUp(oauth2SignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), SIGNUP_SUCCESS.getMessage()));
    }

    @GetMapping("/id/duplicate")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkId(String id) {
        boolean checkId = userSingUpService.checkIdDup(id);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), CHECK_DUP.getMessage(), checkId));
    }

    @GetMapping("/nick/duplicate")
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkNick(String nickName) {
        boolean checkNick = userSingUpService.checkNick(nickName);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), CHECK_DUP.getMessage(), checkNick));
    }

    @PostMapping("/email")
    @ApiOperation(value = "이메일 인증 전송", notes = "이메일 인증을 전송합니다.")
    public ResponseEntity<ResponseDto<String>> sendMail(String email) {
        try {
            String message = emailService.sendSimpleMessage(email);
            return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EMAIL_SEND_SUCCESS.getMessage(), message));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
