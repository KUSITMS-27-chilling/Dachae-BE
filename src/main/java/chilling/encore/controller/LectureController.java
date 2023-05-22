package chilling.encore.controller;

import chilling.encore.domain.Lecture;
import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.*;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.LectureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static chilling.encore.dto.responseMessage.LectureConstants.LectureSuccessMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/lecture")
@Api(tags = "Lecture API")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/my")
    @ApiOperation(value = "사용자가 신청한 우동강 목록", notes = "토큰 필요")
    public ResponseEntity<ResponseDto<List<LectureInfo>>> getMyParticipate() {
        List<LectureInfo> participateLectures = lectureService.getParticipateLecture();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_MY_MESSAGE.getMessage(), participateLectures));
    }

    @GetMapping("/today")
    @ApiOperation(value = "새로 올라온 우동강 목록", notes = "토큰 필수는 아님 -> 로그인 유무에 따라 다르게 진행")
    public ResponseEntity<ResponseDto<List<LectureInfo>>> getTodayLecture() {
        List<LectureInfo> todayLectures = lectureService.getTodayLecture();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_TODAY_MESSAGE.getMessage(), todayLectures));
    }

    @GetMapping(value = {"{category}/page/{page}", "{category}/page"})
    @ApiOperation(value = "카테고리별 우동강 목록", notes = "토큰 필수는 아님 -> 로그인 유무에 따라 다르게 진행")
    public ResponseEntity<ResponseDto<LecturePage>> getLecturePage(@PathVariable String category, @PathVariable @Nullable Integer page) {
        LecturePage lecturePageInfos = lectureService.getLectureInfoWithCategory(page, category);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_SUCCESS_MESSAGE.getMessage(), lecturePageInfos));
    }

    @GetMapping("/detail/images/{lectureIdx}")
    @ApiOperation(value = "상세에서 맨 위 사진", notes = "토큰 필수 X")
    public ResponseEntity<ResponseDto<LectureImages>> getLectureImages(@PathVariable Long lectureIdx) {
        LectureImages images = lectureService.getImages(lectureIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_DETAIL_IMAGES.getMessage(), images));
    }

    @GetMapping("/detail/teacherInfo/{lectureIdx}")
    @ApiOperation(value = "상세에서 강사 정보", notes = "토큰 필수 X")
    public ResponseEntity<ResponseDto<LectureDetailsTeacher>> getDetailTeacherInfo(@PathVariable Long lectureIdx) {
        LectureDetailsTeacher detailsTeacherInfo = lectureService.getTeacherInfo(lectureIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_DETAIL_TEACHER.getMessage(), detailsTeacherInfo));
    }

    @GetMapping("/detail/basicInfo/{lectureIdx}")
    @ApiOperation(value = "상세에서 수업 기본 정보", notes = "토큰 필수 X")
    public ResponseEntity<ResponseDto<LectureBasicInfo>> getBasicInfo(@PathVariable Long lectureIdx) {
        LectureBasicInfo lectureBasicInfo = lectureService.getLectureBasicInfo(lectureIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_DETAIL_BASIC_INFO.getMessage(), lectureBasicInfo));
    }

    }
