package chilling.encore.global.config.security.util;

import chilling.encore.domain.User;
import chilling.encore.global.config.security.jwt.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    public Optional<User> getLoggedInUser() {
        return Optional.ofNullable(
                ((PrincipalDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getUser());
    }

}