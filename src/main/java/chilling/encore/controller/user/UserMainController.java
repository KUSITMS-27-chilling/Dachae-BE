package chilling.encore.controller.user;


import chilling.encore.dto.UserDto.*;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.user.UserMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static chilling.encore.dto.responseMessage.UserConstants.SuccessMessage.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "USER_MAIN API")
public class UserMainController {
    private final UserMainService userMainService;

    @GetMapping("/grade")
    @ApiOperation(value = "회원 등급 조회", notes = "등급과 관심 분야")
    public ResponseEntity<ResponseDto<UserGrade>> selectGrade() {
        UserGrade grade = userMainService.getGrade();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_GRADE_SUCCESS.getMessage(), grade));
    }

    @GetMapping("/learning-info")
    @ApiOperation(value = "배움 정보 조회", notes = "토큰 필수")
    public ResponseEntity<ResponseDto<List<LearningInfo>>> selectLearningInfo() {
        List<LearningInfo> learningInfos = userMainService.getLearningInfo();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_LEARNING_INFO_SUCCESS.getMessage(), learningInfos));
    }

    @GetMapping("/regions")
    @ApiOperation(value = "유저 지역 모두 조회", notes = "로그인 하지 않은 경우 인기 지역 조회")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectFavRegion() {
        UserFavRegion favCenter = userMainService.getFavCenter();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }

    @GetMapping("/region")
    @ApiOperation(value = "유저 거주지 조회", notes = "유저의 거주지 확인 -> 토큰 필수")
    public ResponseEntity<ResponseDto<UserRegion>> selectRegion() {
        UserRegion region = userMainService.getRegion();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REGION_SUCCESS.getMessage(), region));
    }

    @GetMapping("/fav-regions")
    @ApiOperation(value = "거주지 이외의 관심지역 조회 (관심지역 수정시 사용)", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto<UserFavRegion>> selectOnlyFavRegion() {
        UserFavRegion favCenter = userMainService.onlyFavRegion();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_REGION_SUCCESS.getMessage(), favCenter));
    }
}
