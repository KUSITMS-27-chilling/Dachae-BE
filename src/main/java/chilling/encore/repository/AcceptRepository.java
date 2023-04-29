package chilling.encore.repository;

import chilling.encore.domain.Acceptors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcceptRepository extends JpaRepository<Acceptors, Long> {
}
