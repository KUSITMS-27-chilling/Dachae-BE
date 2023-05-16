package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.Lecture;
import chilling.encore.domain.LectureMessage;
import chilling.encore.domain.User;
import chilling.encore.dto.LectureDto.LectureInfo;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.LectureRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository  userRepository;
    private final CenterRepository centerRepository;

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

    public List<LectureInfo> getTodayLecture() {
        LocalDate now = LocalDate.now();
        try {
            User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("Not Login"));
            String region = user.getRegion();
            if (user.getFavRegion() != null)
                region = region + "," + user.getFavRegion();
            String[] regions = region.split(",");
            List<LectureInfo> lectureInfos = lectureRepository.findTodayLectureWithRegion(now, regions);
            return lectureInfos;
        } catch (ClassCastException e) {
            List<Center> centers = centerRepository.findTop4ByOrderByFavCountDesc();
            String[] regions = {centers.get(0).getRegion(), centers.get(1).getRegion(), centers.get(2).getRegion(), centers.get(3).getRegion()};
            List<LectureInfo> lectureInfos = lectureRepository.findTodayLectureWithRegion(now, regions);
            return lectureInfos;
        }
    }
}
