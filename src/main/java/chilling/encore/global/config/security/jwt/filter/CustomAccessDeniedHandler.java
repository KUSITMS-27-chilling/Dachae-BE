package chilling.encore.global.config.security.jwt.filter;

import chilling.encore.global.config.security.exception.SecurityException.AuthorityException;
import chilling.encore.global.config.slack.SlackMessage;
import chilling.encore.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode;
import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final SlackMessage slackMessage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("인증에 실패했습니다.");
        AuthorityException authorityException = new AuthorityException();
        slackMessage.sendSlackAlertErrorLog(authorityException, JwtExcpetionCode.NON_AUTHORITY.getCode(), request);
        response.setStatus(authorityException.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(authorityException.getErrorCode(), authorityException.getMessage()));
        response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));
    }
}
