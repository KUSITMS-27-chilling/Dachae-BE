package chilling.encore.controller;

import chilling.encore.dto.ProgramDto.AllProgramInCenter;
import chilling.encore.dto.ProgramDto.AllProgramMainResponses;
import chilling.encore.dto.ProgramDto.NewProgramsResponse;
import chilling.encore.dto.ProgramDto.PagingPrograms;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.service.ProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.dto.responseMessage.ProgramConstant.SuccessMessage.SELECT_NEW_SUCCESS;
import static chilling.encore.dto.responseMessage.ProgramConstant.SuccessMessage.SELECT_PROGRAM_SUCCESS;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/program")
@Api(tags = "Program API")
public class ProgramController {
    private final ProgramService programService;

    @GetMapping(value = {"/new", "/new/{region}"})
    @ApiOperation(value = "센터 새소식 조회", notes = "/new 는 기본 새소식, /new/{region}은 다른 지역 선택")
    public ResponseEntity<ResponseDto<NewProgramsResponse>> getNewPrograms(@Nullable @PathVariable String region) {
        NewProgramsResponse newPrograms = programService.getNewPrograms(region);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_NEW_SUCCESS.getMessage(), newPrograms));
    }

    @GetMapping()
    @ApiOperation(value = "모아보기 각 센터 프로그램 조회", notes = "3개씩")
    public ResponseEntity<ResponseDto<AllProgramMainResponses>> getPrograms() {
        AllProgramMainResponses centerPrograms = programService.getCenterPrograms();
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_PROGRAM_SUCCESS.getMessage(), centerPrograms));
    }

    @GetMapping(value = {"/{region}/page", "/{region}/page/{page}"})
    @ApiOperation(value = "상세보기 각 센터 프로그램 조회", notes = "6개씩")
    public ResponseEntity<ResponseDto<PagingPrograms>> getDetailsPrograms(@PathVariable String region, @PathVariable @Nullable Integer page) {
        if (page == null)
            page = 1;
        PagingPrograms pagingPrograms = programService.getPagingProgram(region, page);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_PROGRAM_SUCCESS.getMessage(), pagingPrograms));
    }

    @GetMapping("/{region}")
    @ApiOperation(value = "센터의 모든 프로그램 조회", notes = "글 작성시 모집중인 프로그램 모두 확인")
    public ResponseEntity<ResponseDto<AllProgramInCenter>> getAllProgram(@PathVariable String region) {
        AllProgramInCenter allProgram = programService.getAllProgram(region);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_PROGRAM_SUCCESS.getMessage(), allProgram));
    }

}
