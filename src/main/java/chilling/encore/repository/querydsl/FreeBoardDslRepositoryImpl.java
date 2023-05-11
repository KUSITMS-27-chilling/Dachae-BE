package chilling.encore.repository.querydsl;

import chilling.encore.domain.FreeBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static chilling.encore.domain.QFreeBoard.freeBoard;

@RequiredArgsConstructor
@Repository
@Slf4j
public class FreeBoardDslRepositoryImpl implements FreeBoardDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FreeBoard> findRegionFreeBoard(String region, String orderBy, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!region.equals("all")) {
            builder.and(freeBoard.region.eq(region));
        }
        JPAQuery<FreeBoard> query =
                queryFactory.selectFrom(freeBoard)
                .where(builder)
                .orderBy(orderSpecifier(orderBy));

        List<FreeBoard> freeBoards = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.fetchCount();

        return new PageImpl<>(freeBoards, pageable, total);

    }
    private OrderSpecifier<?> orderSpecifier(String orderBy) {
        if (orderBy == null)
            return new OrderSpecifier<>(Order.DESC, freeBoard.hit);
        switch (orderBy) {
            case "date" :
                return new OrderSpecifier<>(Order.DESC, freeBoard.createdAt);
            case "hit" :
                return new OrderSpecifier<>(Order.DESC, freeBoard.hit);
            case "comment" :
                return new OrderSpecifier<>(Order.DESC, freeBoard.freeBoardComments.size());
        }
        return null;
    }

    @Override
    public List<FreeBoard> findPopularFreeBoard() {
        NumberExpression<Integer> hitCount = freeBoard.hit;
        NumberExpression<Integer> commentCount = freeBoard.freeBoardComments.size();
        Expression<Integer> total = hitCount.add(commentCount);
        OrderSpecifier<Integer> orderByTotalDesc = Expressions.numberPath(Integer.class, total.toString()).desc();

        return queryFactory.selectFrom(freeBoard)
                .limit(3L)
                .orderBy(orderByTotalDesc)
                .fetch();
    }

}
