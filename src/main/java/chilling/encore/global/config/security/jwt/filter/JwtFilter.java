package chilling.encore.global.config.security.jwt.filter;

import chilling.encore.global.config.security.exception.SecurityException;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode;
import chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage;
import chilling.encore.global.config.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        if (!requestURI.contains("/user/logout")) {
            try {
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) && tokenProvider.checkBlackList(jwt)) {
                    //jwt를 가지고 있는가, 올바른가, 블랙리스트에 등록되어있는건 아닌가
                    if (requestURI.contains("/user/re-issue")) {
                        log.info("Token 재발급 진행시 유효성 검사");
                        checkRefreshTokenAndReIssueAccessToken(jwt);
                    } else {
                        tokenProvider.checkMultiLogin(jwt);
                    }

                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }
            } catch (SecurityException.RemovedAccessTokenException e) {
                log.warn("RemovedAccessToken reject");
                log.error("exception : {}", e.getMessage());
                throw e;
            } catch (SecurityException | MalformedJwtException e) {
                log.error("exception : {}", e.getMessage());
                throw new SecurityException.InvalidJwtFormatException();
            } catch (ExpiredJwtException e) {
                log.error("exception : {}", e.getMessage());
                throw new SecurityException.ExpiredJwtException();
            } catch (UnsupportedJwtException e) {
                log.error("exception : {}", e.getMessage());
                throw new SecurityException.NonSupportedJwtException();
            } catch (IllegalArgumentException e) {
                log.error("exception : {}", e.getMessage());
                throw new SecurityException.WrongTokenException();
            } catch (Exception e) {
                log.error("exception : {}", e.getMessage());
                throw new SecurityException.UnKnownException();
            }
        }

        filterChain.doFilter(request, response);
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
