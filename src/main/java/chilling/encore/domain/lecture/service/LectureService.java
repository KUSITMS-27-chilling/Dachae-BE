package chilling.encore.domain.lecture.service;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.teacherInfo.entity.TeacherInfo;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.lecture.dto.LectureDto.*;
import chilling.encore.domain.lecture.exception.LectureException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.lecture.repository.jpa.LectureRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
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
    private final SecurityUtils securityUtils;
    public List<LectureInfo> getParticipateLecture() {
        User user = userRepository.findById(securityUtils.getLoggedInUser()
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
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("Not Login"));
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

        lectureObjectives = getLectureInfos(lecture.getLectureObjective(), lectureObjectives);
        lectureContents = getLectureInfos(lecture.getLectureContent(), lectureContents);
        lectureMethods = getLectureInfos(lecture.getLectureMethod(), lectureMethods);
        lectureRequireds = getLectureInfos(lecture.getLectureRequired(), lectureRequireds);
        return new List[]{lectureObjectives, lectureContents, lectureMethods, lectureRequireds};
    }

    private static List<String> getLectureInfos(String lecture, List<String> lectureInfos) {
        String lectureInfo = lecture;
        if (lectureInfo != null)
            lectureInfos = List.of(lectureInfo.split(","));
        return lectureInfos;
    }
}
