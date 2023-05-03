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

        //조회수
//        int hit = listenTogether.getHit();
        //ListenTogether의 지역(region)
//        Program program = listenTogether.getProgram();
//        LearningCenter learningCenter = program.getLearningCenter();

        QListenTogether qListenTogether = QListenTogether.listenTogether;
        QLearningCenter qLearningCenter = QLearningCenter.learningCenter;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLearningCenter.region.like("%" + region + "%"));
//        Builder.and(qUser.favRegion.like("%" + favRegion + "%"));

        List<ListenTogether> results = query.selectFrom(QListenTogether.listenTogether)
                .where(builder)
                .orderBy(qListenTogether.hit.desc())
                .limit(3)
                .fetch();

        return results;
        // 1. User.favRegion 받아와서
        // 2. split(",")하고
        // 3. for 돌려서
        // 4. 하나의 List<ListenTogether>에 담아?
        // 5. List<ListenTogether>에서 top3 뽑기
    }
}
