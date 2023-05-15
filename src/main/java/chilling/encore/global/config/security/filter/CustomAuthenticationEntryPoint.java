package chilling.encore.global.config.security.filter;

import chilling.encore.global.config.security.exception.SecurityException;
import chilling.encore.global.config.security.jwt.JwtConstants;
import chilling.encore.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        SecurityException.WrongTokenException e = new SecurityException.WrongTokenException(
                JwtConstants.JwtExcpetionMessage.WRONG_TOKEN.getMessage(),
                JwtConstants.JwtExcpetionCode.WRONG_TOKEN.getCode(),
                HttpStatus.FORBIDDEN);

        response.setStatus(e.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorResponseJson = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));
        response.getOutputStream().write(errorResponseJson.getBytes("UTF-8"));
    }
}
