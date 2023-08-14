package chilling.encore.domain.lecture.controller;

import chilling.encore.domain.lecture.dto.LectureMessageDto.CreatedLectureMessage;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.lecture.service.LectureMessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.domain.lecture.constant.LectureMessageConstant.LectureMessageSuccessMessage.CREATE_SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "LectureMessage API")
@RequestMapping("/lecture")
public class LectureMessageController {
    private final LectureMessageService lectureMessageService;
    @PostMapping("/{lectureIdx}/message")
    public ResponseEntity<ResponseDto> sendMessage(@RequestBody CreatedLectureMessage createdLectureMessage, @PathVariable Long lectureIdx) {
        lectureMessageService.save(createdLectureMessage, lectureIdx);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), CREATE_SUCCESS_MESSAGE.getMessage()));
    }
}
