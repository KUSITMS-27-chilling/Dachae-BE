package chilling.encore.controller;

import chilling.encore.dto.DetailDto.DetailResponse;
import chilling.encore.dto.ProgramDto;
import chilling.encore.dto.ProgramDto.GetDetailPrograms;
import chilling.encore.dto.ProgramDto.PagingPrograms;
import chilling.encore.dto.responseMessage.DetailConstant;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.DetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.DetailConstant.SuccessMessage.DETAIL_SELECT_SUCCESS;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/detail")
@Api(tags = "DetailPage API")
public class DetailController {
    private final DetailsService detailsService;

    @GetMapping("/{region}")
    @ApiOperation(value = "모아보기의 상세보기", notes = "상세보기 첫 진입 요청")
    public ResponseEntity<ResponseDto<DetailResponse>> getDetails(@PathVariable String region) {
        DetailResponse details = detailsService.firstDetails(region);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), DETAIL_SELECT_SUCCESS.getMessage(), details));
    }

    @GetMapping("/{region}/program/{page}")
    @ApiOperation(value = "프로그램 페이징", notes = "프로그램의 페이지 조절")
    public ResponseEntity<ResponseDto<PagingPrograms>> programPaging(@PathVariable String region, @PathVariable Integer page) {
        PagingPrograms pagingPrograms = detailsService.getProgramPaging(region, page);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), DETAIL_SELECT_SUCCESS.getMessage(), pagingPrograms));
    }
}
