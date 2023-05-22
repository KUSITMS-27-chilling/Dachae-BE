package chilling.encore.controller;

import chilling.encore.dto.AlaramDto.AlarmResponse;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.AlarmConstants.SuccessMessage.SELECT_ALARM_SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/alarm")
@Api(tags = "Alarm API")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping()
    @ApiOperation(value = "알람 새소식 조회", notes = "있다면 값이 들어있고 없다면 빈값")
    public ResponseEntity<ResponseDto<AlarmResponse>> getAlarm() {
        AlarmResponse alarms = alarmService.getAlarm();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), SELECT_ALARM_SUCCESS.getMessage(), alarms));
    }
}
