package chilling.encore.repository.querydsl;

import chilling.encore.domain.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
public class ListenTogetherDslRepositoryImpl implements ListenTogetherDslRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ListenTogetherDslRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<ListenTogether> findTop3ByOrderByHitDesc(String region) {

        QListenTogether qListenTogether = QListenTogether.listenTogether;
        QLearningCenter qLearningCenter = QLearningCenter.learningCenter;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLearningCenter.region.like("%" + region + "%"));

        List<ListenTogether> results = query.selectFrom(QListenTogether.listenTogether)
                .where(builder)
                .orderBy(qListenTogether.hit.desc())
                .limit(3)
                .fetch();

        return results;
    }
}
