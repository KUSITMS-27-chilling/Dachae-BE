package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestRepository extends JpaRepository<Suggest, Long> {
}
