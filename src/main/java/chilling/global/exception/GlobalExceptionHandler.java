package chilling.global.exception;

import chilling.global.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Objects.requireNonNull;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorCode = requireNonNull(ex.getFieldError()).getDefaultMessage();
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ErrorResponse(errorCode, ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String errorCode = "405 METHOD_NOT_ALLOWED";
        String message = "클라이언트가 사용한 HTTP 메서드가 리소스에서 허용되지 않습니다.";
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String errorCode = "500 INTERNAL_SERVER_ERROR";
        String message = "서버에서 요청을 처리하는 동안 오류가 발생했습니다.";
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
