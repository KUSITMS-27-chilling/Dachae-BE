package chilling.encore.global.exception;

import chilling.encore.global.dto.ErrorResponse;
import chilling.encore.global.dto.ResponseDto;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final Slack slackClient = Slack.getInstance();
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @Value("${slack.webhook.url}")
    private String webHookUrl;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> LoginException(AuthenticationException ex, HttpServletRequest request) {
        String errorCode = String.valueOf(SELECT_SUCCESS_CODE.getCode());
        log.warn(LOG_FORMAT, ex.getClass(), errorCode, ex.getMessage());
        return ResponseEntity.ok(new ErrorResponse(errorCode, NOT_FOUND_USER.getMessage()));
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResponse> AuthorizationException(ClassCastException ex, HttpServletRequest request) {
        String errorCode = String.valueOf(AUTHORIZATION_FAIL_CODE.getCode());
        log.warn(LOG_FORMAT, ex.getClass(), errorCode, ex.getMessage());
        String stackTrace = getStackTraceAsString(ex);
        sendSlackAlertErrorLog(ex, errorCode, stackTrace, request);
        return ResponseEntity.ok(new ErrorResponse(String.valueOf(AUTHORIZATION_FAIL_CODE.getCode()), AUTHORIZATION_FAIL.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, HttpServletRequest request) {
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), ex.getErrorCode(), ex.getMessage());
        String stackTrace = getStackTraceAsString(ex);
        sendSlackAlertErrorLog(ex, ex.getErrorCode(), stackTrace, request);
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorCode = requireNonNull(ex.getFieldError()).getDefaultMessage();
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        String stackTrace = getStackTraceAsString(ex);
        sendSlackAlertErrorLog(ex, errorCode, stackTrace, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ErrorResponse(errorCode, ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String errorCode = "405 METHOD_NOT_ALLOWED";
        String message = "클라이언트가 사용한 HTTP 메서드가 리소스에서 허용되지 않습니다.";
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        String stackTrace = getStackTraceAsString(ex);
        sendSlackAlertErrorLog(ex, errorCode, stackTrace, request);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        String errorCode = "500 INTERNAL_SERVER_ERROR";
        String message = "서버에서 요청을 처리하는 동안 오류가 발생했습니다.";
        log.warn(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode, ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        String stackTrace = getStackTraceAsString(ex);
        sendSlackAlertErrorLog(ex, errorCode, stackTrace, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void sendSlackAlertErrorLog(Exception e, String errorCode, String stackTrace, HttpServletRequest request) {
        log.info("e : {} - code : {} - stackTrace : {} - request : {}", e, errorCode, stackTrace, request);
        try {
            slackClient.send(webHookUrl, Payload.builder()
                    .text("서버 에러 발생!! 백엔드팀 확인 요망")
                    .attachments(
                            List.of(generateSlackAttachment(e, errorCode, stackTrace, request))
                    )
                    .build());
        } catch (IOException slackError) {
            log.error("Slack 통신 에러 발생");
        }
    }
    
    //attach 생성 -> Field를 리스트로 담자
    private Attachment generateSlackAttachment(Exception e, String errorCode, String stackTrace, HttpServletRequest request) throws IOException {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null)
            ip = request.getRemoteAddr();
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Request IP", ip),
                        generateSlackField("Method", request.getMethod()),
                        generateSlackField("Request URL", String.valueOf(request.getRequestURL())),
                        generateSlackField("Error Code", errorCode),
                        generateSlackField("Error Message", e.getMessage()),
                        generateSlackField("StackTrace", stackTrace)
                ))
                .build();
    }

    // Field 생성 메서드
    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
