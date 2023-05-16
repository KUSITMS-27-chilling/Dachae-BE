package chilling.encore.repository.springDataJpa;

import chilling.encore.domain.Lecture;
import chilling.encore.repository.querydsl.LectureDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureDslRepository {
}
