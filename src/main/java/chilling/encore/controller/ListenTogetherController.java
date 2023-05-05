package chilling.encore.controller;

import chilling.encore.dto.ListenTogetherDto;
import chilling.encore.dto.ListenTogetherDto.ListenTogetherPage;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.ListenTogetherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/listen")
@Api(tags = "ListenTogetherPage API")
public class ListenTogetherController {
    private final ListenTogetherService listenTogetherService;

    @GetMapping(value = {"/page/{region}/{page}", "/page/{region}"})
    @ApiOperation(value = "같이할래요 게시글 페이징 조회")
    public ResponseEntity<ResponseDto<ListenTogetherPage>> getListenTogetherPage(@PathVariable String region, @PathVariable @Nullable Integer page) {
        ListenTogetherPage listenTogetherPage = listenTogetherService.getListenTogetherPage(region, page);
        return ResponseEntity.ok(ResponseDto.create(1,"", listenTogetherPage));
    }
}
