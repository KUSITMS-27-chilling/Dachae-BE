package chilling.encore.utils.domain;

import chilling.encore.domain.Program;
import chilling.encore.domain.Review;
import chilling.encore.domain.User;

import java.time.LocalDateTime;

public class MockReview {
    public Review getMockReview(Long idx, User user, Program program) {
        return Review.builder()
                .reviewIdx(idx)
                .user(user)
                .program(program)
                .week(1)
                .title("test")
                .content("test")
                .image("test1,test2,test3")
                .hit(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
