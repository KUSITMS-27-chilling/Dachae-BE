package chilling.encore.global.config.security.jwt;

import chilling.encore.domain.User;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.exception.SecurityException.RemovedAccessTokenException;
import chilling.encore.repository.springDataJpa.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionCode.REMOVE_ACCESS_TOKEN;
import static chilling.encore.global.config.security.jwt.JwtConstants.JwtExcpetionMessage.REMOVED_ACCESS_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {
    private final RedisRepository redisRepository;
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_IDX = "userIdx";
    private static final String ACCESS = "Access";
    private static final String REFRESH = "Refresh";
    private static final String GRANT = "Bearer";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityTime;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityTime;

    private final UserRepository userRepository;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfoResponse createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date current = new Date();

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long userIdx = principal.getUser().getUserIdx();

        String accessToken = createAccessToken(authorities, current, userIdx);
        String refreshToken = createRefreshToken(authorities, current, userIdx);

        updateRefreshToken(userIdx, refreshToken);

        return TokenInfoResponse.from(GRANT, accessToken, refreshToken, accessTokenValidityTime);
    }

    public TokenInfoResponse oauth2CreateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date current = new Date();

        Oauth2PrincipalDetails principal = (Oauth2PrincipalDetails) authentication.getPrincipal();
        Long userIdx = principal.getUser().getUserIdx();

        String accessToken = createAccessToken(authorities, current, userIdx);
        String refreshToken = createRefreshToken(authorities, current, userIdx);

        updateRefreshToken(userIdx, refreshToken);

        return TokenInfoResponse.from(GRANT, accessToken, refreshToken, accessTokenValidityTime);
    }

    private String createRefreshToken(String authorities, Date current, Long userIdx) {
        Date refreshTokenValidity = new Date(current.getTime() + this.refreshTokenValidityTime);

        String refreshToken = Jwts.builder()
                .setSubject(REFRESH)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(USER_IDX, userIdx)
                .setIssuedAt(current)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshTokenValidity)
                .compact();
        return refreshToken;
    }

    private String createAccessToken(String authorities, Date current, Long userIdx) {
        Date accessTokenValidity = new Date(current.getTime() + this.accessTokenValidityTime);

        String accessToken = Jwts.builder()
                .setSubject(ACCESS)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(USER_IDX, userIdx)
                .setIssuedAt(current)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTokenValidity)
                .compact();

        log.info("accessToken 저장");
        redisRepository.setValues(ACCESS + userIdx.toString(), accessToken, Duration.ofSeconds(accessTokenValidityTime));
        return accessToken;
    }

    public void updateRefreshToken(Long userIdx, String refreshToken) {
        try {
            log.info("refreshToken 저장");
            redisRepository.setValues(String.valueOf(userIdx), refreshToken, Duration.ofSeconds(refreshTokenValidityTime));
        } catch (NoSuchElementException e) {
            log.error("일치하는 회원이 없습니다.");
            throw e;
        }
    }

    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        String userIdx = String.valueOf(claims.get(USER_IDX));

        User user = this.userRepository.findById(Long.parseLong(userIdx))
                .orElseThrow();

        return new UsernamePasswordAuthenticationToken(new PrincipalDetails(user), token, authorities);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            checkMultiLogin(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw e;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }



    public boolean validateRefreshToken(String token) {
        if (!getSubject(token).equals(REFRESH))
            throw new IllegalArgumentException("Refresh Token이 아닙니다.");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            String userIdx = getUserIdx(token);
            String redisToken = redisRepository.getValues(userIdx).orElseThrow();
            if (!redisToken.equals(token))
                throw new IllegalArgumentException("Refresh Token이 다릅니다.");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw e;
        } catch (NoSuchElementException e) {
            log.error("유효하지 않은 JWT 입니다.");
            throw e;
        }
    }

    public boolean checkBlackList(String token) {
        if (redisRepository.checkBlackList(token).isPresent())
            throw new IllegalArgumentException("로그아웃된 유저입니다.");
        return true;
    }

    private void checkMultiLogin(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        String userIdx = claims.getBody().get(USER_IDX).toString();
        log.info("checkMultiple Login");
        if (!redisRepository.getValues(ACCESS + userIdx).equals(token))
            throw new RemovedAccessTokenException(REMOVED_ACCESS_TOKEN.getMessage(), REMOVE_ACCESS_TOKEN.getCode(), HttpStatus.FORBIDDEN);
    }

    public String getSubject(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String getUserIdx(String token) {
        Claims claims = parseClaims(token);

        return claims.get(USER_IDX).toString();
    }

    public Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
