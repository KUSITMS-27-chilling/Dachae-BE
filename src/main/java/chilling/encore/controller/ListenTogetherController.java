package chilling.encore.controller;

import chilling.encore.dto.ListenTogetherDto;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.ListenTogetherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.ListenTogetherConstants.ListenTogetherSuccessMessage.LISTEN_TOGETHER_SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/community/listen")
@Api(tags = "ListenTogetherPage API")
public class ListenTogetherController {
    private final ListenTogetherService listenTogetherService;

    @GetMapping()
    @ApiOperation(value = "같이할래요페이지 접근")
    public ResponseEntity<ResponseDto<ListenTogetherDto.ListenTogetherResponse>> getListenTogetherPage() {
        ListenTogetherDto.ListenTogetherResponse listenTogetherPage = listenTogetherService.getListenTogetherPage();
        return ResponseEntity.ok(ResponseDto.create(LISTEN_TOGETHER_SUCCESS_MESSAGE.getMessage(), listenTogetherPage));
    }
}
