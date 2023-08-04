package chilling.encore.service.lecture;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.User;
import chilling.encore.dto.LectureMessageDto.CreatedLectureMessage;
import chilling.encore.exception.LectureException;
import chilling.encore.exception.LectureException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.LectureMessageRepository;
import chilling.encore.repository.springDataJpa.LectureRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
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
    private SecurityUtils securityUtils = new SecurityUtils();
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
