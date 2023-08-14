package chilling.encore.utils.domain;

import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.comments.review.entity.ReviewComments;
import chilling.encore.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Long getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(Long reviewIdx) {
        this.reviewIdx = reviewIdx;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public List<ReviewComments> getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(List<ReviewComments> reviewComments) {
        this.reviewComments = reviewComments;
    }
}
