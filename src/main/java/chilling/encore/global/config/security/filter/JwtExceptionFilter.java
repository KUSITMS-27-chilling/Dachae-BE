package chilling.encore.global.config.security.filter;

import chilling.encore.global.config.security.exception.SecurityException;
import chilling.encore.global.config.security.exception.SecurityException.*;
import chilling.encore.global.dto.ErrorResponse;
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
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (InvalidJwtFormatException e) {
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (NonSupportedJwtException e) {
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (WrongTokenException e) {
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));

        } catch (UnKnownException e) {
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
            response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));
        }
    }
}
