package chilling.encore.controller;

import chilling.encore.dto.FreeBoardDto;
import chilling.encore.dto.FreeBoardDto.AllFreeBoards;
import chilling.encore.service.FreeBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/free")
@Api(tags = "FreeBoard API")
public class FreeBoardController {
    private final FreeBoardService freeBoardService;

    @GetMapping(value = {"/page/{region}/{page}/{orderBy}", "/page/{region}"})
    @ApiOperation(value = "자유게시판 페이지 조회", notes = "첫 진입시 page, orderBy 조건 필요 없음, /page/{region}")
    public AllFreeBoards getFreeBoar(@PathVariable String region, @PathVariable @Nullable Integer page, @PathVariable @Nullable String orderBy) {
        AllFreeBoards freeBoardPage = freeBoardService.getFreeBoardPage(page, region, orderBy);
        return freeBoardPage;
    }
}
