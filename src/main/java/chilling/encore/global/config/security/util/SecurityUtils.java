package chilling.encore.global.config.security.util;

import chilling.encore.domain.User;
import chilling.encore.global.config.jwt.PrincipalDetails;
import chilling.encore.global.exception.ApplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class SecurityUtils {

    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(
                ((PrincipalDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getUser());
    }

}