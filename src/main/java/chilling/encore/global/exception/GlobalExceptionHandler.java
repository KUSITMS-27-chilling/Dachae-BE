package chilling.encore.global.exception;

import chilling.encore.dto.responseMessage.UserConstants.UserFailCode;
import chilling.encore.dto.responseMessage.UserConstants.UserFailMessage;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage;
import chilling.encore.global.config.slack.SlackMessage;
import chilling.encore.global.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.requireNonNull;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final SlackMessage slackMessage;
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> LoginException(AuthenticationException ex, HttpServletRequest request) {
        String errorCode = UserFailCode.NOT_FOUND_USER.getCode();
        log.error(LOG_FORMAT, ex.getClass(), errorCode, ex.getMessage());
        slackMessage.sendSlackAlertErrorLog(ex, errorCode, request);
        return ResponseEntity.ok(new ErrorResponse(errorCode, UserFailMessage.NOT_FOUND_USER.getMessage()));
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResponse> AuthorizationException(ClassCastException ex, HttpServletRequest request) {
        String errorCode = JwtExcpetionCode.WRONG_TOKEN.getCode();
        log.error(LOG_FORMAT, ex.getClass(), errorCode, JwtExcpetionMessage.WRONG_TOKEN);
        slackMessage.sendSlackAlertErrorLog(ex, errorCode, request);
        return ResponseEntity.ok(new ErrorResponse(errorCode, JwtExcpetionMessage.WRONG_TOKEN.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, HttpServletRequest request) {
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), ex.getErrorCode(), ex.getMessage());
        slackMessage.sendSlackAlertErrorLog(ex, ex.getErrorCode(), request);
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorCode = requireNonNull(ex.getFieldError()).getDefaultMessage();
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        slackMessage.sendSlackAlertErrorLog(ex, errorCode, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ErrorResponse(errorCode, ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String errorCode = "405 METHOD_NOT_ALLOWED";
        String message = "클라이언트가 사용한 HTTP 메서드가 리소스에서 허용되지 않습니다.";
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        slackMessage.sendSlackAlertErrorLog(ex, errorCode, request);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        String errorCode = "500 INTERNAL_SERVER_ERROR";
        String message = "서버에서 요청을 처리하는 동안 오류가 발생했습니다.";
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        slackMessage.sendSlackAlertErrorLog(ex, errorCode, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
