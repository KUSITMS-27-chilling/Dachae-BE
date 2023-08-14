package chilling.encore.domain.listenTogether.service;

import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.listenTogether.dto.ParticipantDto;
import chilling.encore.domain.listenTogether.exception.ListenException.NoSuchIdxException;
import chilling.encore.domain.listenTogether.exception.ParticipantException.DuplicateParticipationException;
import chilling.encore.domain.listenTogether.exception.ParticipantException.FullParticipantException;
import chilling.encore.domain.listenTogether.exception.ParticipantException.MyListenTogetherException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.listenTogether.repository.jpa.ListenTogetherRepository;
import chilling.encore.domain.listenTogether.repository.jpa.ParticipantsRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParticipantService {
    private final ParticipantsRepository participantsRepository;
    private final UserRepository userRepository;
    private final ListenTogetherRepository listenTogetherRepository;
    private final RedisRepository redisRepository;
    private final SecurityUtils securityUtils;
    private final String USER_PLUS = "LearningInfo";

    public void upParticipants(ParticipantDto.ParticipantRequest participantRequest) {
        User user = userRepository.findById(
                securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        log.info("listenTogetherIdx = {}", participantRequest.getListenTogetherIdx());
        ListenTogether listenTogether = listenTogetherRepository.findById(participantRequest.getListenTogetherIdx()).orElseThrow(() -> new NoSuchIdxException());
        validate(user, listenTogether);

        Participants participants = Participants.builder()
                .listenTogether(listenTogether)
                .user(user)
                .build();
        participantsRepository.save(participants);
        addLearningInfo(user, listenTogether);
    }

    private void validate(User user, ListenTogether listenTogether) {
        if (listenTogether.getParticipants().size() >= listenTogether.getGoalNum())
            throw new FullParticipantException();
        if (participantsRepository.existsByUserAndListenTogether(user, listenTogether))
            throw new DuplicateParticipationException();
        if (listenTogether.getUser().getUserIdx() == user.getUserIdx())
            throw new MyListenTogetherException();
    }

    private void addLearningInfo(User user, ListenTogether listenTogether) {
        String isFin = "false";
        redisRepository.addLearningInfo(
                listenTogether.getUser().getUserIdx().toString() + USER_PLUS,
                UUID.randomUUID().toString(),
                listenTogether.getTitle(),
                isFin,
                listenTogether.getListenIdx().toString(),
                user.getNickName(),
                LocalDateTime.now()
        );

        if (listenTogether.getGoalNum() - 1 == listenTogether.getParticipants().size()) {
            isFin = "true";
            redisRepository.addLearningInfo(
                    listenTogether.getUser().getUserIdx().toString() + USER_PLUS,
                    UUID.randomUUID().toString(),
                    listenTogether.getTitle(),
                    isFin,
                    listenTogether.getListenIdx().toString(),
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
