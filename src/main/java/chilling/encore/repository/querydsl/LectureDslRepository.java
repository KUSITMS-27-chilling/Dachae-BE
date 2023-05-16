package chilling.encore.repository.querydsl;

import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.LectureInfo;

import java.time.LocalDate;
import java.util.List;

public interface LectureDslRepository {
    List<LectureInfo> findTodayLectureWithRegion(LocalDate now, String[] regions);
}
