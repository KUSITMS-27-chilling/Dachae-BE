package chilling.encore.controller;


import chilling.encore.domain.User;
import chilling.encore.dto.UserDto.*;
import chilling.encore.global.config.email.EmailService;
import chilling.encore.service.UserService;
import chilling.encore.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.*;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

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
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/signup/oauth2")
    @ApiOperation(value = "oauth2 회원 가입", notes = "oauth2 추가 정보 받은 이후 회원가입 진행")
    public ResponseEntity<ResponseDto> signUp(@RequestBody Oauth2SignUpRequest oauth2SignUpRequest) {
        User user = userService.oauth2signUp(oauth2SignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), SIGNUP_SUCCESS.getMessage()));
    }

    @GetMapping("/checkIdDup")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkId(String id) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        SELECT_SUCCESS_CODE.getCode(),
                        CHECK_DUP.getMessage(),
                        userService.checkIdDup(id)
                )
        );
    }

    @GetMapping("/checkNickDup")
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkNick(String nickName) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        SELECT_SUCCESS_CODE.getCode(),
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
            return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), EMAIL_SEND_SUCCESS.getMessage(), s));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 진행")
    public ResponseEntity<ResponseDto<UserLoginResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse loginResponse = userService.login(userLoginRequest);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), LOGIN_SUCCESS.getMessage(), loginResponse));
    }

    @GetMapping("/grade")
    @ApiOperation(value = "회원 등급 조회", notes = "등급과 관심 분야")
    public ResponseEntity<ResponseDto<UserGrade>> selectGrade() {
        UserGrade grade = userService.getGrade();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_GRADE_SUCCESS.getMessage(), grade));
    }

    @GetMapping("/regions")
    @ApiOperation(value = "유저 관심 지역 조회", notes = "로그인 하지 않은 경우 인기 지역 조회")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectFavRegion() {
        UserFavRegion favCenter = userService.getFavCenter();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }

    @GetMapping("/favRegions")
    @ApiOperation(value = "거주지 이외의 관심지역 조회 (관심지역 수정시 사용)", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectOnlyFavRegion() {
        UserFavRegion favCenter = userService.onlyFavRegion();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }

    @PutMapping("/edit/regions")
    @ApiOperation(value = "유저 관심 지역 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavRegion(@RequestBody EditFavRegion editFavRegion) {
        userService.editFavRegion(editFavRegion);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), EDIT_REGION_SUCCESS.getMessage()));
    }

    @PutMapping("/edit/nickName")
    @ApiOperation(value = "유저 닉네임 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editNickName(@RequestBody EditNickName editNickName) {
        userService.editNickName(editNickName);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), EDIT_NICKNAME_SUCCESS.getMessage()));
    }

    @PutMapping("/edit/favField")
    @ApiOperation(value = "유저 활동 분야 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavField(@RequestBody EditFavField editFavField) {
        userService.editFavField(editFavField);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), EDIT_FAV_FIELD_SUCCESS.getMessage()));
    }

    @GetMapping("/userInfo")
    @ApiOperation(value = "마이페이지에서 유저 정보 조회", notes = "프로필, 닉네임, 유저 등급 등등")
    public ResponseEntity<ResponseDto<UserInfo>> getUserInfo() {
        UserInfo userInfo = userService.getUserInfo();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_INFO_SUCCESS.getMessage(), userInfo));
    }
}
