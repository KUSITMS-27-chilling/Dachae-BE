package chilling.encore.utils.domain;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;

import java.time.LocalDateTime;

public class MockListenTogether {
    public ListenTogether getMockListenTogether(Long idx, User user, Program mockProgram) {

        return ListenTogether.builder()
                .listenIdx(idx)
                .user(user)
                .program(mockProgram)
                .content("test")
                .hit(1)
                .goalNum(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
