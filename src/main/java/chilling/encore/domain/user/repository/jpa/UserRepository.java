package chilling.encore.domain.user.repository.jpa;

import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.user.repository.querydsl.UserDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDslRepository{
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickName(String nickName);

}
