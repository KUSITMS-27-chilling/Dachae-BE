package chilling.encore.global.config.jwt;

import chilling.encore.global.config.jwt.JwtConstants.JWTExceptionList;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static chilling.encore.global.config.jwt.JwtConstants.JWTExceptionList.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) && tokenProvider.checkBlackList(jwt)) {
                //jwt를 가지고 있는가, 올바른가, 블랙리스트에 등록되어있는건 아닌가
                if (requestURI.contains("/user/reIssueToken")) {
                    log.info("Token 재발급 진행시 유효성 검사");
                    checkRefreshTokenAndReIssueAccessToken(jwt);
                }

                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", WRONG_TYPE_TOKEN.getErrorCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", EXPIRED_TOKEN.getErrorCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", UNSUPPORTED_TOKEN.getErrorCode());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", WRONG_TOKEN.getErrorCode());
        } catch (Exception e) {
            log.error("================================================");
            log.error("JwtFilter - doFilterInternal() 오류발생");
            log.error("token : {}", jwt);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            request.setAttribute("exception", UNKNOWN_ERROR.getErrorCode());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void checkRefreshTokenAndReIssueAccessToken(String token) {
        try {
            tokenProvider.validateRefreshToken(token);
        } catch (Exception e) {
            log.error("예외 발생 {}", e.getMessage());
            throw e;
        }
    }
}
