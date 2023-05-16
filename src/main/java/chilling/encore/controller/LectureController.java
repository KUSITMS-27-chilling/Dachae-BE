package chilling.encore.controller;

import chilling.encore.domain.Lecture;
import chilling.encore.dto.LectureDto.LectureInfo;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
