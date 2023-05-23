package chilling.encore.global.config.security.filter;

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
                    if (requestURI.contains("/user/reIssueToken")) {
                        log.info("Token 재발급 진행시 유효성 검사");
                        checkRefreshTokenAndReIssueAccessToken(jwt);
                    }

                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                }
            } catch (SecurityException | MalformedJwtException e) {
                log.info("exception", e.getMessage());
                throw new SecurityException.InvalidJwtFormatException(JwtExcpetionMessage.INVALID_FORMAT.getMessage(), JwtExcpetionCode.INVALID_FORMAT.getCode(), HttpStatus.FORBIDDEN);
            } catch (ExpiredJwtException e) {
                log.info("exception", e.getMessage());
                throw new SecurityException.ExpiredJwtException(JwtExcpetionMessage.JWT_EXPIRED.getMessage(), JWT_EXPIRED.getCode(), HttpStatus.FORBIDDEN);
            } catch (UnsupportedJwtException e) {
                log.info("exception", e.getMessage());
                throw new SecurityException.NonSupportedJwtException(JwtExcpetionMessage.JWT_NOT_SUPPORTED.getMessage(), JWT_NOT_SUPPORTED.getCode(), HttpStatus.FORBIDDEN);
            } catch (IllegalArgumentException e) {
                log.info("exception", e.getMessage());
                throw new SecurityException.WrongTokenException(JwtExcpetionMessage.WRONG_TOKEN.getMessage(), WRONG_TOKEN.getCode(), HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                throw new SecurityException.UnKnownException(JwtExcpetionMessage.UNKHOWN_EXCEPTION.getMessage(), UNKHOWN_EXCEPTION.getCode(), HttpStatus.FORBIDDEN);
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
