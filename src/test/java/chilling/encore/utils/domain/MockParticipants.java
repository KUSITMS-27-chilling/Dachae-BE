package chilling.encore.utils.domain;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Participants;
import chilling.encore.domain.User;

public class MockParticipants {
    public Participants getMockParticipants(Long idx, ListenTogether listenTogether, User user) {
        return Participants.builder()
                .participantsIdx(idx)
                .user(user)
                .listenTogether(listenTogether)
                .build();
    }
}
