package chilling.encore.repository;

import chilling.encore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickName(String nickName);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
