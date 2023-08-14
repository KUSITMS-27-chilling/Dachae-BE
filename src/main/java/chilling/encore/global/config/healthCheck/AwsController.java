package chilling.encore.global.config.healthCheck;

import chilling.encore.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsController {
    @GetMapping("/health")
    public ResponseEntity<ResponseDto> healthCheck() {
        return ResponseEntity.ok(ResponseDto.create(HttpStatus.OK.value(), "OK"));
    }
}
