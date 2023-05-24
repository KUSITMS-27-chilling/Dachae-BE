package chilling.encore.controller;


import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.dto.UserDto.*;
import chilling.encore.global.config.email.EmailService;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.*;

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
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), SIGNUP_SUCCESS.getMessage()));
    }

    @PostMapping("/signup/oauth2")
    @ApiOperation(value = "oauth2 회원 가입", notes = "oauth2 추가 정보 받은 이후 회원가입 진행")
    public ResponseEntity<ResponseDto> signUp(@RequestBody Oauth2SignUpRequest oauth2SignUpRequest) {
        User user = userService.oauth2signUp(oauth2SignUpRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), SIGNUP_SUCCESS.getMessage()));
    }

    @GetMapping("/id/duplicate")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkId(String id) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        HttpStatus.OK.value(),
                        CHECK_DUP.getMessage(),
                        userService.checkIdDup(id)
                )
        );
    }

    @GetMapping("/nick/duplicate")
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크를 합니다.")
    public ResponseEntity<ResponseDto<Boolean>> checkNick(String nickName) {
        return ResponseEntity.ok(
                ResponseDto.create(
                        HttpStatus.OK.value(),
                        CHECK_DUP.getMessage(),
                        userService.checkNick(nickName)
                )
        );
    }

    @PostMapping("/email")
    @ApiOperation(value = "이메일 인증 전송", notes = "이메일 인증을 전송합니다.")
    public ResponseEntity<ResponseDto<String>> sendMail(String email) {
        try {
            log.info("email = {}", email);
            String s = emailService.sendSimpleMessage(email);
            return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EMAIL_SEND_SUCCESS.getMessage(), s));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 진행")
    public ResponseEntity<ResponseDto<UserLoginResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse loginResponse = userService.login(userLoginRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), LOGIN_SUCCESS.getMessage(), loginResponse));
    }

    @GetMapping("/grade")
    @ApiOperation(value = "회원 등급 조회", notes = "등급과 관심 분야")
    public ResponseEntity<ResponseDto<UserGrade>> selectGrade() {
        UserGrade grade = userService.getGrade();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_GRADE_SUCCESS.getMessage(), grade));
    }

    @GetMapping("/learning-info")
    @ApiOperation(value = "배움 정보 조회", notes = "토큰 필수")
    public ResponseEntity<ResponseDto<List<LearningInfo>>> selectLearningInfo() {
        List<LearningInfo> learningInfos = userService.getLearningInfo();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_LEARNING_INFO_SUCCESS.getMessage(), learningInfos));
    }

    @GetMapping("/regions")
    @ApiOperation(value = "유저 관심 지역 조회", notes = "로그인 하지 않은 경우 인기 지역 조회")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectFavRegion() {
        UserFavRegion favCenter = userService.getFavCenter();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }

    @GetMapping("/fav-regions")
    @ApiOperation(value = "거주지 이외의 관심지역 조회 (관심지역 수정시 사용)", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectOnlyFavRegion() {
        UserFavRegion favCenter = userService.onlyFavRegion();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }

    @PutMapping("/regions")
    @ApiOperation(value = "유저 관심 지역 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavRegion(@RequestBody EditFavRegion editFavRegion) {
        userService.editFavRegion(editFavRegion);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_REGION_SUCCESS.getMessage()));
    }

    @PutMapping("/nick")
    @ApiOperation(value = "유저 닉네임 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editNickName(@RequestBody EditNickName editNickName) {
        userService.editNickName(editNickName);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_NICKNAME_SUCCESS.getMessage()));
    }

    @PutMapping("/fav-field")
    @ApiOperation(value = "유저 활동 분야 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavField(@RequestBody EditFavField editFavField) {
        userService.editFavField(editFavField);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_FAV_FIELD_SUCCESS.getMessage()));
    }

    @GetMapping("/info")
    @ApiOperation(value = "마이페이지에서 유저 정보 조회", notes = "프로필, 닉네임, 유저 등급 등등")
    public ResponseEntity<ResponseDto<UserInfo>> getUserInfo() {
        UserInfo userInfo = userService.getUserInfo();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), userInfo));
    }

    @GetMapping("/participants")
    @ApiOperation(value = "마이페이지 유저가 참여한 회수 조회")
    public ResponseEntity<ResponseDto<GetTotalParticipants>> getUserParticipant() {
        GetTotalParticipants userParticipant = userService.getUserParticipant();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), userParticipant));
    }

    @GetMapping("/my-post")
    @ApiOperation(value = "마이페이지 유저의 작성글 조회", notes = "전체 ~ 각각")
    public ResponseEntity<ResponseDto<UserDto.GetTotalWrite>> getTotalWrite() {
        UserDto.GetTotalWrite total = userService.getTotalWrite();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), total));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "accessToken 혹은 refreshToken 필요")
    public ResponseEntity<ResponseDto> logout(@RequestHeader("Authorization") String authorization) {
        userService.logout(authorization);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(),LOGOUT_SUCCESS.getMessage()));
    }
    
    @PostMapping("/re-issue")
    @ApiOperation(value = "토큰 재발급", notes = "refreshToken 보내줘야 함")
    public ResponseEntity<ResponseDto<UserLoginResponse>> reIssueToken() {
        UserLoginResponse userLoginResponse = userService.reIssueToken();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), REISSUE_SUCCESS.getMessage(), userLoginResponse));
    }
}
