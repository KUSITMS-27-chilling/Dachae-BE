package chilling.encore.domain.user.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserDslRespositoryImpl {
    private final JPAQueryFactory queryFactory;

//    public void get() {
//        queryFactory.selectFrom(QUser.user);
//    }
//    이런식으로 queryFactory 사용하는데 객체는 Q타입으로 생성된 객체 사용해야해

}
