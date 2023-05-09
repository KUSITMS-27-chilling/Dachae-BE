package chilling.encore.global.exception;

import chilling.encore.global.dto.ErrorResponse;
import chilling.encore.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static chilling.encore.dto.responseMessage.ListenTogetherConstants.ListenTogetherFailMessage.SAVE_FAIL_MESSAGE;
import static chilling.encore.dto.responseMessage.UserConstants.UserFailMessage.AUTHORIZATION_FAIL;
import static chilling.encore.dto.responseMessage.UserConstants.UserFailMessage.NOT_FOUND_USER;
import static chilling.encore.global.dto.ResponseCode.globalFailCode.AUTHORIZATION_FAIL_CODE;
import static chilling.encore.global.dto.ResponseCode.globalFailCode.SERVER_ERROR;
import static chilling.encore.global.dto.ResponseCode.globalSuccessCode.SELECT_SUCCESS_CODE;
import static java.util.Objects.requireNonNull;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> LoginException(AuthenticationException ex) {
        String errorCode = String.valueOf(SELECT_SUCCESS_CODE.getCode());
        log.warn(LOG_FORMAT, ex.getClass(), errorCode, ex.getMessage());
        return ResponseEntity.ok(new ErrorResponse(errorCode, NOT_FOUND_USER.getMessage()));
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResponse> AuthorizationException(ClassCastException ex) {
        String errorCode = String.valueOf(AUTHORIZATION_FAIL_CODE.getCode());
        log.warn(LOG_FORMAT, ex.getClass(), errorCode, ex.getMessage());
        return ResponseEntity.ok(new ErrorResponse(String.valueOf(AUTHORIZATION_FAIL_CODE.getCode()), AUTHORIZATION_FAIL.getMessage()));
    }

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
        ex.printStackTrace();
        String errorCode = "500 INTERNAL_SERVER_ERROR";
        String message = "서버에서 요청을 처리하는 동안 오류가 발생했습니다.";
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
