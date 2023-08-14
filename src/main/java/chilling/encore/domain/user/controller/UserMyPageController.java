package chilling.encore.domain.user.controller;

import chilling.encore.domain.user.dto.UserDto;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.user.service.UserMyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.domain.user.constant.UserConstants.SuccessMessage.*;
import static chilling.encore.domain.user.constant.UserConstants.SuccessMessage.SELECT_INFO_SUCCESS;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "USER_MY_PAGE")
public class UserMyPageController {
    private final UserMyPageService userMyPageService;

    @PutMapping("/fav-regions")
    @ApiOperation(value = "유저 관심 지역 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavRegion(@RequestBody UserDto.EditFavRegion editFavRegion) {
        userMyPageService.editFavRegion(editFavRegion);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_REGION_SUCCESS.getMessage()));
    }

    @PutMapping("/nick")
    @ApiOperation(value = "유저 닉네임 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editNickName(@RequestBody UserDto.EditNickName editNickName) {
        userMyPageService.editNickName(editNickName);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_NICKNAME_SUCCESS.getMessage()));
    }

    @PutMapping("/fav-field")
    @ApiOperation(value = "유저 활동 분야 수정", notes = "로그인 하지 않은 경우 요청 X")
    public ResponseEntity<ResponseDto> editFavField(@RequestBody UserDto.EditFavField editFavField) {
        userMyPageService.editFavField(editFavField);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), EDIT_FAV_FIELD_SUCCESS.getMessage()));
    }

    @GetMapping("/info")
    @ApiOperation(value = "마이페이지에서 유저 정보 조회", notes = "프로필, 닉네임, 유저 등급 등등")
    public ResponseEntity<ResponseDto<UserDto.UserInfo>> getUserInfo() {
        UserDto.UserInfo userInfo = userMyPageService.getUserInfo();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), userInfo));
    }

    @GetMapping("/participants")
    @ApiOperation(value = "마이페이지 유저가 참여한 회수 조회")
    public ResponseEntity<ResponseDto<UserDto.GetTotalParticipants>> getUserParticipant() {
        UserDto.GetTotalParticipants userParticipant = userMyPageService.getUserParticipant();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), userParticipant));
    }

    @GetMapping("/my-post")
    @ApiOperation(value = "마이페이지 유저의 작성글 조회", notes = "전체 ~ 각각")
    public ResponseEntity<ResponseDto<UserDto.GetTotalWrite>> getTotalWrite() {
        UserDto.GetTotalWrite total = userMyPageService.getTotalWrite();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_INFO_SUCCESS.getMessage(), total));
    }
}
