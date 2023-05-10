package chilling.encore.service;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Participants;
import chilling.encore.domain.User;
import chilling.encore.dto.ParticipantDto;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ParticipantsRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
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
        if (listenTogether.getParticipants().size() >= listenTogether.getGoalNum())
            throw new IllegalArgumentException("더이상 불가능 (나중에 제대로 예외 처리)");

        if (participantsRepository.existsByUserAndListenTogether(user, listenTogether))
            throw new IllegalArgumentException();
        if (listenTogether.getUser().getUserIdx() == user.getUserIdx())
            throw new IllegalArgumentException();

        Participants participants = Participants.builder()
                .listenTogether(listenTogether)
                .user(user)
                .build();
        participantsRepository.save(participants);
    }
}
