package chilling.encore.repository.querydsl;

import chilling.encore.domain.Review;
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

import static chilling.encore.domain.QProgram.program;
import static chilling.encore.domain.QReview.review;
import static chilling.encore.domain.QUser.user;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReviewDslRepositoryImpl implements ReviewDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findRegionReviewPage(String[] region, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < region.length; i++) {
            builder.or(program.learningCenter.region.eq(region[i]));
        }
        JPAQuery<Review> query = queryFactory.selectFrom(review)
                .leftJoin(review.user, user)
                .leftJoin(review.program, program)
                .where(builder)
                .orderBy(review.createdAt.desc());
        List<Review> reviews = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.fetchCount();

        return new PageImpl<>(reviews, pageable, total);
    }

    @Override
    public List<Review> findRegionReview(String[] region) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < region.length; i++) {
            builder.or(program.learningCenter.region.eq(region[i]));
        }
        return queryFactory.selectFrom(review)
                .leftJoin(review.user, user)
                .leftJoin(review.program, program)
                .where(builder)
                .fetch();
    }
}
