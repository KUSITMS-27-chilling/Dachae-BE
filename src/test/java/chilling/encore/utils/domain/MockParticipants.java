package chilling.encore.utils.domain;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.user.entity.User;

public class MockParticipants {
    public Participants getMockParticipants(Long idx, ListenTogether listenTogether, User user) {
        return Participants.builder()
                .participantsIdx(idx)
                .user(user)
                .listenTogether(listenTogether)
                .build();
    }
}
