package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.ListenTogether;
import chilling.encore.repository.querydsl.ListenTogetherDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenTogetherRepository extends JpaRepository<ListenTogether, Long>, ListenTogetherDslRepository {
    List<ListenTogether> findTop3ByUser_UserIdxOrderByCreatedAtDesc(Long userIdx);
}
