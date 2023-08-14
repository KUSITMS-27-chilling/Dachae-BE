package chilling.encore.domain.lecture.service;

import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.lecture.dto.LectureMessageDto.CreatedLectureMessage;
import chilling.encore.domain.lecture.exception.LectureException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.lecture.repository.jpa.LectureMessageRepository;
import chilling.encore.domain.lecture.repository.jpa.LectureRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LectureMessageService {
    private final UserRepository userRepository;
    private final LectureMessageRepository lectureMessageRepository;
    private final LectureRepository lectureRepository;
    private final SecurityUtils securityUtils;
    public void save(CreatedLectureMessage createdLectureMessage, Long lectureIdx) {
        User user = userRepository.findById(securityUtils.getLoggedInUser()
                .orElseThrow(() -> new ClassCastException("NotLogin"))
                .getUserIdx()).get();
        Lecture lecture = lectureRepository.findById(lectureIdx).orElseThrow(() -> new NoSuchIdxException());

        LectureMessage lectureMessage = CreatedLectureMessage.to(lecture, user, createdLectureMessage);
        lectureMessageRepository.save(lectureMessage);

        user.updateGrade();
    }
}
