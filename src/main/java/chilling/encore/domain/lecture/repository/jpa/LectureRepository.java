package chilling.encore.domain.lecture.repository.jpa;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.repository.querydsl.LectureDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureDslRepository {
}
