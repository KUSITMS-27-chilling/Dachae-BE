package chilling.encore.domain.lecture.repository.querydsl;

import chilling.encore.domain.lecture.dto.LectureDto.LectureInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static chilling.encore.domain.lecture.entity.QLecture.lecture;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LectureDslRepositoryImpl implements LectureDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<LectureInfo> findTodayLectureWithRegion(LocalDate now, String[] regions) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < regions.length; i++) {
            builder.or(lecture.region.contains(regions[i]));
        }
        builder.and(lecture.createdAt.eq(now));

        List<LectureInfo> lectureInfos = queryFactory.select(Projections.constructor(LectureInfo.class,
                        lecture.lectureIdx,
                        lecture.title,
                        lecture.category,
                        lecture.teacherInfo.profile,
                        lecture.teacherInfo.user.name,
                        lecture.teacherInfo.years,
                        lecture.teacherInfo.introduce))
                .from(lecture)
                .where(builder)
                .fetch();
        return lectureInfos;
    }

    @Override
    public Page<LectureInfo> findTop10LectureWithCategoryAndRegion(String category, String[] regions, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < regions.length; i++) {
            builder.or(lecture.region.contains(regions[i]));
        }
        builder.and(lecture.category.eq(category));
        JPAQuery<LectureInfo> query = queryFactory.select(Projections.constructor(LectureInfo.class,
                        lecture.lectureIdx,
                        lecture.title,
                        lecture.category,
                        lecture.teacherInfo.profile,
                        lecture.teacherInfo.user.name,
                        lecture.teacherInfo.years,
                        lecture.teacherInfo.introduce))
                .from(lecture)
                .where(builder)
                .orderBy(lecture.createdAt.desc());

        List<LectureInfo> lectureInfos = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.fetchCount();

        return new PageImpl<>(lectureInfos, pageable, total);
    }
}
