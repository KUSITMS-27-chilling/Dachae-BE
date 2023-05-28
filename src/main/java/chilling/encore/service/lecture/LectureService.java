package chilling.encore.service.lecture;

import chilling.encore.domain.*;
import chilling.encore.dto.LectureDto.*;
import chilling.encore.exception.LectureException;
import chilling.encore.exception.LectureException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.LectureRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            return getLogin(now);
        } catch (ClassCastException e) {
            return getNotLogin(now);
        }
    }

    private List<LectureInfo> getNotLogin(LocalDate now) {
        String[] regions = notLoginRegions();
        List<LectureInfo> lectureInfos = lectureRepository.findTodayLectureWithRegion(now, regions);
        return lectureInfos;
    }

    @NotNull
    private String[] notLoginRegions() {
        List<Center> centers = centerRepository.findTop4ByOrderByFavCountDesc();
        String[] regions = {centers.get(0).getRegion(), centers.get(1).getRegion(), centers.get(2).getRegion(), centers.get(3).getRegion()};
        return regions;
    }

    private List<LectureInfo> getLogin(LocalDate now) {
        String[] regions = loginRegions();
        List<LectureInfo> lectureInfos = lectureRepository.findTodayLectureWithRegion(now, regions);
        return lectureInfos;
    }

    @NotNull
    private String[] loginRegions() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("Not Login"));
        String region = user.getRegion();
        if (user.getFavRegion() != null)
            region = region + "," + user.getFavRegion();
        String[] regions = region.split(",");
        return regions;
    }

    public LecturePage getLectureInfoWithCategory(Integer page, String category) {
        String[] regions;
        if (page == null)
            page = 1;
        try {
            regions = loginRegions();
        } catch (ClassCastException e) {
            regions = notLoginRegions();
        }
        log.info("regions = {}, regions.size = {}", regions, regions.length);
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<LectureInfo> lectureInfos = lectureRepository.findTop10LectureWithCategoryAndRegion(category, regions, pageable);
        return LecturePage.from(lectureInfos.getTotalPages(), lectureInfos.getContent());
    }

    public LectureDetailsTeacher getTeacherInfo(Long lectureIdx) {
        TeacherInfo teacherInfo = lectureRepository.findById(lectureIdx)
                                        .orElseThrow(() -> new NoSuchIdxException()).getTeacherInfo();
        List<String> careers = new ArrayList<>();
        List<String> certificates = new ArrayList<>();
        if (teacherInfo.getCareer() != null)
            careers = List.of(teacherInfo.getCareer().split(","));
        if (teacherInfo.getCertificate() != null)
            certificates = List.of(teacherInfo.getCertificate().split(","));
        return LectureDetailsTeacher.from(teacherInfo.getProfile(), careers, certificates);
    }

    public LectureImages getImages(Long lectureIdx) {
        Lecture lecture = lectureRepository.findById(lectureIdx).orElseThrow(() -> new NoSuchIdxException());
        List<String> images = new ArrayList<>();
        if (lecture.getImage() != null)
            images = List.of(lecture.getImage().split(","));
        return LectureImages.builder().images(images).build();
    }

    public LectureBasicInfo getLectureBasicInfo(Long lectureIdx) {
        Lecture lecture = lectureRepository.findById(lectureIdx).orElseThrow(() -> new NoSuchIdxException());
        return LectureBasicInfo.from(lecture, getProceeds(lecture));
    }

    private List<String>[] getProceeds(Lecture lecture) {
        List<String> lectureObjectives = new ArrayList<>();
        List<String> lectureContents = new ArrayList<>();
        List<String> lectureMethods = new ArrayList<>();
        List<String> lectureRequireds = new ArrayList<>();

        String lectureObjective = lecture.getLectureObjective();
        if (lectureObjective != null)
            lectureObjectives = List.of(lectureObjective.split(","));
        String lectureContent = lecture.getLectureContent();
        if (lectureContent != null)
            lectureContents = List.of(lectureContent.split(","));
        String lectureMethod = lecture.getLectureMethod();
        if (lectureMethod != null)
            lectureMethods = List.of(lectureMethod.split(","));
        String lectureRequired = lecture.getLectureRequired();
        if (lectureRequired != null)
            lectureRequireds = List.of(lectureRequired.split(","));
        return new List[]{lectureObjectives, lectureContents, lectureMethods, lectureRequireds};
    }
}
