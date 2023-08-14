package chilling.encore.domain.lecture.repository.querydsl;

import chilling.encore.domain.lecture.dto.LectureDto.LectureInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LectureDslRepository {
    List<LectureInfo> findTodayLectureWithRegion(LocalDate now, String[] regions);
    Page<LectureInfo> findTop10LectureWithCategoryAndRegion(String category, String[] regions, Pageable pageable);
}
