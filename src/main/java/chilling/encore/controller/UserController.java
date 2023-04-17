package chilling.encore.controller;


import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.global.config.email.EmailService;
import chilling.encore.service.UserService;
import chilling.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static chilling.encore.dto.responseMessage.UserResponseMessage.SuccessMessage.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Api(tags = "USER API")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    //회원가입 Controller
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 완료합니다.")
    public ResponseEntity<ResponseDto<User>> signUp(@RequestBody UserDto userDto) {
        userService.signUp(userDto);
        return ResponseEntity.ok(ResponseDto.create(SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/checkIdDup")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkId(@RequestBody Map<String, String> id) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        CHECK_DUP.getMessage(),
                        userService.checkIdDup(id.get("id"))
                )
        );
    }

    @PostMapping("/checkNickDup")
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkNick(@RequestBody Map<String, String> nickName) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        CHECK_DUP.getMessage(),
                        userService.checkNick(nickName.get("nickName"))
                )
        );
    }

    @PostMapping("/sendMail")
    @ApiOperation(value = "이메일 인증 전송", notes = "이메일 인증을 전송합니다.")
    public ResponseEntity<ResponseDto<String>> sendMail(@RequestBody Map<String, String> email) {
        try {
            String s = emailService.sendSimpleMessage(email.get("email"));
            return ResponseEntity.ok(ResponseDto.create(EMAIL_SEND_SUCCESS.getMessage(), s));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
