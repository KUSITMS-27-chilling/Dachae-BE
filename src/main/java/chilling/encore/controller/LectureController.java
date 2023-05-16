package chilling.encore.controller;

import chilling.encore.domain.Lecture;
import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.LectureInfo;
import chilling.encore.dto.LectureDto.LecturePage;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.LectureService;
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
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/my")
    public ResponseEntity<ResponseDto<List<LectureInfo>>> getMyParticipate() {
        List<LectureInfo> participateLectures = lectureService.getParticipateLecture();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_MY_MESSAGE.getMessage(), participateLectures));
    }

    @GetMapping("/today")
    public ResponseEntity<ResponseDto<List<LectureInfo>>> getTodayLecture() {
        List<LectureInfo> todayLectures = lectureService.getTodayLecture();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_TODAY_MESSAGE.getMessage(), todayLectures));
    }

    @GetMapping(value = {"{category}/page/{page}", "{category}/page"})
    public ResponseEntity<ResponseDto<LecturePage>> getLecturePage(@PathVariable String category, @PathVariable @Nullable Integer page) {
        LecturePage lecturePageInfos = lectureService.getLectureInfoWithCategory(page, category);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_SUCCESS_MESSAGE.getMessage(), lecturePageInfos));
    }
}
