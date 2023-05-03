package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.User;
import chilling.encore.repository.querydsl.UserDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDslRepository{
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickName(String nickName);

}
