package chilling.encore.global.config.security.util;

import chilling.encore.domain.User;
import chilling.encore.global.config.security.jwt.PrincipalDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class SecurityUtils {

    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(
                ((PrincipalDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getUser());
    }

}