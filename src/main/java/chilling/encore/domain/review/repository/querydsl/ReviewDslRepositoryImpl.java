package chilling.encore.domain.review.repository.querydsl;

import chilling.encore.domain.review.entity.Review;
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

import static chilling.encore.domain.program.entity.QProgram.program;
import static chilling.encore.domain.review.entity.QReview.review;
import static chilling.encore.domain.user.entity.QUser.user;

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
    public List<Review> findRegionReview(List<String> region) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < region.size(); i++) {
            builder.or(program.learningCenter.region.eq(region.get(i)));
        }
        return queryFactory.selectFrom(review)
                .where(builder)
                .orderBy(review.hit.desc())
                .limit(3)
                .fetch();
    }
}
