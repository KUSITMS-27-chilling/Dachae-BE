package chilling.encore.domain.listenTogether.repository.querydsl;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static chilling.encore.domain.listenTogether.entity.QListenTogether.listenTogether;
import static chilling.encore.domain.program.entity.QProgram.program;
import static chilling.encore.domain.user.entity.QUser.user;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ListenTogetherDslRepositoryImpl implements ListenTogetherDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ListenTogether> findRegionListenTogether(String[] region, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < region.length; i++) {
            builder.or(program.learningCenter.region.eq(region[i]));
        }
        JPAQuery<ListenTogether> query = queryFactory.selectFrom(listenTogether)
                .leftJoin(listenTogether.user, user)
                .leftJoin(listenTogether.program, program)
                .where(builder)
                .orderBy(listenTogether.createdAt.desc()); // 정렬 조건 추가

        List<ListenTogether> listenTogethers = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.fetchCount();

        return new PageImpl<>(listenTogethers, pageable, total);
    }

    @Override
    public List<ListenTogether> findPopularListenTogether(List<String> regions) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < regions.size(); i++) {
            builder.or(program.learningCenter.region.eq(regions.get(i)));
        }
        return queryFactory.selectFrom(listenTogether)
                .where(builder)
                .limit(3L)
                .orderBy(listenTogether.hit.desc())
                .fetch();
    }
}
