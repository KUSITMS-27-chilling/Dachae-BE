package chilling.encore.domain.center.controller;

import chilling.encore.domain.center.dto.CenterDto.CenterInfo;
import chilling.encore.global.dto.ResponseDto;
import chilling.encore.domain.center.service.CenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chilling.encore.domain.center.constant.CenterConstants.SuccessMessage.SELECT_INFO_SUCCESS;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/center")
@Api(tags = "Center API")
public class CenterController {
    private final CenterService centerService;

    @GetMapping("/{region}/info")
    @ApiOperation(value = "센서 관심수 전화번호 조회")
    public ResponseEntity<ResponseDto<CenterInfo>> getCenterInfo(@PathVariable String region) {
        CenterInfo centerInfo = centerService.getCenterInfo(region);
        return ResponseEntity.ok(ResponseDto.create(SELECT_SUCCESS_CODE.getCode(), SELECT_INFO_SUCCESS.getMessage(), centerInfo));
    }
}
