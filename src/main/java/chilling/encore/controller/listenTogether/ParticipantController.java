package chilling.encore.controller.listenTogether;

import chilling.encore.dto.ParticipantDto.ParticipantRequest;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.listenTogether.ParticipantService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.ParticipantConstants.SuccessMessage.CREATE_SUCCESS_MESSAGE;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.CREATE_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/listen")
@Api(tags = "Participant API")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/participant")
    public ResponseEntity<ResponseDto> upParticipant(@RequestBody ParticipantRequest participantRequest) {
        participantService.upParticipants(participantRequest);
        return ResponseEntity.ok(ResponseDto.create(CREATE_SUCCESS_CODE.getCode(), CREATE_SUCCESS_MESSAGE.getMessage()));
    }
}
