package chilling.encore.utils.domain;

import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.comments.review.entity.ReviewComments;
import chilling.encore.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MockReview extends Review{
    public MockReview(Long reviewIdx, User user, Program program) {
        this.reviewIdx = reviewIdx;
        this.user = user;
        this.program = program;
    }

    private Long reviewIdx;
    private User user;
    private Program program;
    private int week = 1;
    private String title = "test";
    private String content = "test";
    private String image = "test1,test2,test3";
    private int hit = 1;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private List<ReviewComments> reviewComments = new ArrayList<>();
}
