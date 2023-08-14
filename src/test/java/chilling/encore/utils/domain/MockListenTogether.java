package chilling.encore.utils.domain;

import chilling.encore.domain.comments.listenTogether.entity.ListenComments;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockListenTogether extends ListenTogether {
    public MockListenTogether(Long listenIdx, User user, Program program) {
        this.listenIdx = listenIdx;
        this.user = user;
        this.program = program;
    }
    private Long listenIdx;
    private User user;
    private Program program;
    private String title = "test";
    private String content = "test";
    private int hit = 10;
    private int goalNum = 10;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    List<ListenComments> listexnComments = new ArrayList<>();
    List<Participants> participants = new ArrayList<>();

    public void setListenIdx(Long listenIdx) {
        this.listenIdx = listenIdx;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setGoalNum(int goalNum) {
        this.goalNum = goalNum;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setListexnComments(List<ListenComments> listexnComments) {
        this.listexnComments = listexnComments;
    }

    public void setParticipants(List<Participants> participants) {
        this.participants = participants;
    }

    @Override
    public Long getListenIdx() {
        return listenIdx;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Program getProgram() {
        return program;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getHit() {
        return hit;
    }

    @Override
    public int getGoalNum() {
        return goalNum;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public List<ListenComments> getListexnComments() {
        return listexnComments;
    }

    @Override
    public List<Participants> getParticipants() {
        return participants;
    }
}
