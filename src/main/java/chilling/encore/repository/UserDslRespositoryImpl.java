package chilling.encore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@Slf4j
public class UserDslRespositoryImpl {
    private final JPAQueryFactory queryFactory;

    public UserDslRespositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
