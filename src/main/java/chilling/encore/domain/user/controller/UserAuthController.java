package chilling.encore.domain.user.controller;

import chilling.encore.domain.user.dto.UserDto;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.user.service.UserAuthService;
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
@Api(tags = "USER_AUTH API")
public class UserAuthController {
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 진행")
    public ResponseEntity<ResponseDto<UserDto.UserLoginResponse>> login(@RequestBody UserDto.UserLoginRequest userLoginRequest) {
        UserDto.UserLoginResponse loginResponse = userAuthService.login(userLoginRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), LOGIN_SUCCESS.getMessage(), loginResponse));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "accessToken 혹은 refreshToken 필요")
    public ResponseEntity<ResponseDto> logout(@RequestHeader("Authorization") String authorization) {
        userAuthService.logout(authorization);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(),LOGOUT_SUCCESS.getMessage()));
    }

    @PostMapping("/re-issue")
    @ApiOperation(value = "토큰 재발급", notes = "refreshToken 보내줘야 함")
    public ResponseEntity<ResponseDto<UserDto.UserLoginResponse>> reIssueToken() {
        UserDto.UserLoginResponse userLoginResponse = userAuthService.reIssueToken();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), REISSUE_SUCCESS.getMessage(), userLoginResponse));
    }
}
