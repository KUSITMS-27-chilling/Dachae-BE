package chilling.encore.controller;

import chilling.encore.dto.FreeBoardDto;
import chilling.encore.dto.FreeBoardDto.AllFreeBoards;
import chilling.encore.dto.FreeBoardDto.CreateFreeBoardRequest;
import chilling.encore.dto.FreeBoardDto.PopularFreeBoards;
import chilling.encore.dto.responseMessage.FreeBoardConstants;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.FreeBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import static chilling.encore.dto.responseMessage.FreeBoardConstants.SuccessMessage.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/free")
@Api(tags = "FreeBoard API")
public class FreeBoardController {
    private final FreeBoardService freeBoardService;

    @GetMapping(value = {"/page/{region}/{page}/{orderBy}", "/page/{region}"})
    @ApiOperation(value = "자유게시판 페이지 조회", notes = "첫 진입시 page, orderBy 조건 필요 없음, /page/{region} : 첫 진입시 region = all (전체 지역 조회)")
    public ResponseEntity<ResponseDto<AllFreeBoards>> getFreeBoar(@PathVariable String region, @PathVariable @Nullable Integer page, @PathVariable @Nullable String orderBy) {
        AllFreeBoards freeBoardPage = freeBoardService.getFreeBoardPage(page, region, orderBy);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), FREE_SELECT_SUCCESS.getMessage(), freeBoardPage));
    }

    @GetMapping("/popular")
    @ApiOperation(value = "자유게시판 인기글 조회", notes = "로그인X의 경우 인기지역 조회")
    public ResponseEntity<ResponseDto<PopularFreeBoards>> getPopular() {
        PopularFreeBoards popular = freeBoardService.getPopular();
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), POPULAR_SELECT_SUCCESS.getMessage(), popular));
    }
    
    @PostMapping("/save")
    @ApiOperation(value = "자유게시판 글 작성", notes = "로그인X의 경우 접근 불가")
    public ResponseEntity<ResponseDto> save(@RequestBody CreateFreeBoardRequest createFreeBoardRequest) {
        freeBoardService.save(createFreeBoardRequest);
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.CREATED.value(), FREE_CREATE_SUCCESS.getMessage()));
    }
}
