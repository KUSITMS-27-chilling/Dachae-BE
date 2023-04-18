package chilling.encore.global.config.security.util;

import chilling.encore.domain.User;
import chilling.encore.global.config.jwt.PrincipalDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class SecurityUtils {

    public static User getLoggedInUser() {
        try {
            return ((PrincipalDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getUser();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }

}