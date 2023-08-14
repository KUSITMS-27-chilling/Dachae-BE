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

@Data
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
}
