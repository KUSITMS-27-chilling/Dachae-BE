package chilling.encore.domain.listenTogether.repository.jpa;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.listenTogether.repository.querydsl.ListenTogetherDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenTogetherRepository extends JpaRepository<ListenTogether, Long>, ListenTogetherDslRepository {
    List<ListenTogether> findTop3ByUser_UserIdxOrderByHitDesc(Long userIdx);
    List<ListenTogether> findAllByUser(User user);

    ListenTogether findByListenIdx(Long listenIdx);
}
