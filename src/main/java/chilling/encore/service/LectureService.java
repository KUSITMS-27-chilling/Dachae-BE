package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.domain.TeacherInfo;
import chilling.encore.dto.LectureDto;
import chilling.encore.dto.LectureDto.*;
import chilling.encore.dto.responseMessage.LectureConstants;
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

import static chilling.encore.dto.responseMessage.LectureConstants.LectureCategory.*;

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
                                        .orElseThrow().getTeacherInfo();
        List<String> careers = new ArrayList<>();
        List<String> certificates = new ArrayList<>();
        if (teacherInfo.getCareer() != null)
            careers = List.of(teacherInfo.getCareer().split(","));
        if (teacherInfo.getCertificate() != null)
            certificates = List.of(teacherInfo.getCertificate().split(","));
        return LectureDetailsTeacher.from(teacherInfo.getProfile(), careers, certificates);
    }

    public LectureImages getImages(Long lectureIdx) {
        Lecture lecture = lectureRepository.findById(lectureIdx).orElseThrow();
        List<String> images = new ArrayList<>();
        if (lecture.getImage() != null)
            images = List.of(lecture.getImage().split(","));
        return LectureImages.builder().images(images).build();
    }

    public LectureBasicInfo getLectureBasicInfo(Long lectureIdx) {
        Lecture lecture = lectureRepository.findById(lectureIdx).orElseThrow();
        return LectureBasicInfo.from(lecture);
    }
}
