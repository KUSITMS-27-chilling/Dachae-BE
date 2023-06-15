package chilling.encore.global.config.security.filter;

import chilling.encore.global.config.security.exception.SecurityException;
import chilling.encore.global.config.security.exception.SecurityException.*;
import chilling.encore.global.config.slack.SlackMessage;
import chilling.encore.global.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final SlackMessage slackMessage;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            String errorResponseJson = getErrorResponseJson(request, response, e);
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (InvalidJwtFormatException e) {
            String errorResponseJson = getErrorResponseJson(request, response, e);
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (NonSupportedJwtException e) {
            String errorResponseJson = getErrorResponseJson(request, response, e);
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (WrongTokenException e) {
            String errorResponseJson = getErrorResponseJson(request, response, e);
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (UnKnownException e) {
            String errorResponseJson = getErrorResponseJson(request, response, e);
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));
        }
    }

    private String getErrorResponseJson(HttpServletRequest request, HttpServletResponse response, SecurityException e) throws JsonProcessingException {
        response.setStatus(e.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        slackMessage.sendSlackAlertErrorLog(e, e.getErrorCode(), request);
        String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
        return errorResponseJson;
    }
}
