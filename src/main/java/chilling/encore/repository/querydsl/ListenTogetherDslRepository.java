package chilling.encore.repository.querydsl;

import chilling.encore.domain.ListenTogether;

import java.util.List;

public interface ListenTogetherDslRepository {

    List<ListenTogether> findTop3ByOrderByHitDesc(String region);
}
