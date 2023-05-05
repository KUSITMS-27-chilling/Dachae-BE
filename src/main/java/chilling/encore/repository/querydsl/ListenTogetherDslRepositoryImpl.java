package chilling.encore.repository.querydsl;

import chilling.encore.domain.*;
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

import static chilling.encore.domain.QListenTogether.listenTogether;
import static chilling.encore.domain.QProgram.program;
import static chilling.encore.domain.QUser.user;

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
                .where(builder);
        List<ListenTogether> listenTogethers = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.fetchCount();

        return new PageImpl<>(listenTogethers, pageable, total);
    }
}
