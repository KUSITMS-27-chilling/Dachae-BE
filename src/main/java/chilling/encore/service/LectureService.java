package chilling.encore.service;

import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.User;
import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.LectureInfo;
import chilling.encore.dto.LectureDto.MyParticipateLecture;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.LectureRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository  userRepository;

    public List<LectureInfo> getParticipateLecture() {
        User user = userRepository.findById(SecurityUtils.getLoggedInUser()
                .orElseThrow(() -> new ClassCastException("Not Login"))
                .getUserIdx()).get();
        List<LectureMessage> lectureMessages = user.getLectureMessages();
        List<LectureInfo> myParticipateLectures = new ArrayList<>();
        if (lectureMessages.size() == 0)
            return myParticipateLectures;
        for (int i = 0; i < lectureMessages.size(); i++) {
            Lecture lecture = lectureMessages.get(i).getLecture();
            myParticipateLectures.add(LectureInfo.from(lecture));
        }
        return myParticipateLectures;
    }
}
