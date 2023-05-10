package chilling.encore.service;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Participants;
import chilling.encore.domain.User;
import chilling.encore.dto.ParticipantDto;
import chilling.encore.exception.ParticipantException;
import chilling.encore.exception.ParticipantException.DuplicateParticipationException;
import chilling.encore.exception.ParticipantException.FullParticipantException;
import chilling.encore.exception.ParticipantException.MyListenTogetherException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ParticipantsRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParticipantService {
    private final ParticipantsRepository participantsRepository;
    private final UserRepository userRepository;
    private final ListenTogetherRepository listenTogetherRepository;

    public void upParticipants(ParticipantDto.ParticipantRequest participantRequest) {
        User user = userRepository.findById(
                SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        log.info("listenTogetherIdx = {}", participantRequest.getListenTogetherIdx());
        ListenTogether listenTogether = listenTogetherRepository.findById(participantRequest.getListenTogetherIdx()).orElseThrow();
        validate(user, listenTogether);

        Participants participants = Participants.builder()
                .listenTogether(listenTogether)
                .user(user)
                .build();
        participantsRepository.save(participants);
    }

    private void validate(User user, ListenTogether listenTogether) {
        if (listenTogether.getParticipants().size() >= listenTogether.getGoalNum())
            throw new FullParticipantException();
        if (participantsRepository.existsByUserAndListenTogether(user, listenTogether))
            throw new DuplicateParticipationException();
        if (listenTogether.getUser().getUserIdx() == user.getUserIdx())
            throw new MyListenTogetherException();
    }
}
