package chilling.encore.repository.querydsl;

import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.LectureInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LectureDslRepository {
    List<LectureInfo> findTodayLectureWithRegion(LocalDate now, String[] regions);
    Page<LectureInfo> findTop10LectureWithCategoryAndRegion(String category, String[] regions, Pageable pageable);
}
