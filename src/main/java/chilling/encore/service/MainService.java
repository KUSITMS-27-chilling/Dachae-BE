package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.AlaramDto.NewAlarm;
import chilling.encore.dto.MainDto.MainResponse;
import chilling.encore.dto.ProgramDto.NewProgram;
import chilling.encore.dto.ProgramDto.NewProgramsResponse;
import chilling.encore.dto.ProgramDto.ProgramMainResponse;
import chilling.encore.dto.ProgramDto.getPrograms;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.ListenAlarmRepository;
import chilling.encore.repository.springDataJpa.ProgramRepository;
import chilling.encore.repository.springDataJpa.ReviewAlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {
    private final CenterRepository centerRepository;
    private final ProgramRepository programRepository;
    private final ReviewAlarmRepository reviewAlarmRepository;
    private final ListenAlarmRepository listenAlarmRepository;

    private LocalDate yesterDay = LocalDate.now().minusDays(1);


    public NewProgramsResponse getNewPrograms(String region) {
        List<Program> programs = programRepository.findAllByStartDateGreaterThanEqualAndLearningCenter_Region(yesterDay, region);
        List<NewProgram> newPrograms = new ArrayList<>();
        for (int i = 0; i < programs.size(); i++) {
            newPrograms.add(NewProgram.from(programs.get(i)));
        }
        return NewProgramsResponse.from(newPrograms);
    }

    public MainResponse getFirstPage() {
        try {
            /**
             * JWT를 사용할 경우 아래의 SecurityUtils에서 뽑아서 사용할 수 있음
             * 하지만 SecurityContextHolder의 경우 각 스레드마다 독립적으로 존재하는데, 스프링의 경우 각 요청당 하나의 스레드가 생성되기 때문에
             * JWT 요청으로 ContextHolder에 계속해서 저장이 되지 않는 이상 사용할 수 없다...
             */
            User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
            return loginUser(user);
        } catch (ClassCastException e) {
            log.info("{}", e.getMessage());
            return MainResponse.from(
                    null,
                    null,
                    null,
                    notLogin()
            );
        }
    }

    private MainResponse loginUser(User user) {
        String region = user.getRegion();

        List<ReviewAlarm> reviewAlarms = reviewAlarmRepository.findAllByIsReadFalseAndUser_UserIdx(user.getUserIdx());
        List<ListenAlarm> listenAlarms = listenAlarmRepository.findAllByIsReadFalseAndUser_UserIdx(user.getUserIdx());
        List<NewAlarm> newAlarms = getNewAlarms(reviewAlarms, listenAlarms);

        List<Program> programs = programRepository.findAllByStartDateGreaterThanEqualAndLearningCenter_Region(yesterDay, region);
        List<NewProgram> newPrograms = new ArrayList<>();
        for (int i = 0; i < programs.size(); i++) {
            newPrograms.add(NewProgram.from(programs.get(i)));
        }

        String favRegion = user.getFavRegion();
        Map<String, ProgramMainResponse> programDatas = getProgramDatas(region, favRegion);

        return MainResponse.from(
                newAlarms,
                newPrograms,
                user.getGrade(),
                programDatas
        );
    }

    private Map<String, ProgramMainResponse> getProgramDatas(String region, String favRegion) {
        String[] favRegions;
        List<Center> favCenters = new ArrayList<>();
        favCenters.add(centerRepository.findByRegion(region));

        if (favRegion != null) {
            favRegions = favRegion.split(",");

            for (int i = 0; i < favRegions.length; i++) {
                Center center = centerRepository.findByRegion(favRegions[i]);
                favCenters.add(center);
            }
        }

        Map<String, ProgramMainResponse> programDatas = getProgramResponses(favCenters);
        return programDatas;
    }

    private List<NewAlarm> getNewAlarms(List<ReviewAlarm> reviewAlarms, List<ListenAlarm> listenAlarms) {
        List<NewAlarm> newAlarms = new ArrayList<>();

        for (int i = 0; i < reviewAlarms.size(); i++) {
            NewAlarm alarm = NewAlarm.from(
                    null,
                    reviewAlarms.get(i).getReviewAlarmIdx(),
                    reviewAlarms.get(i).getUser().getNickName()
            );
            newAlarms.add(alarm);
        }
        for (int i = 0; i < listenAlarms.size(); i++) {
            NewAlarm alarm = NewAlarm.from(
                    listenAlarms.get(i).getListenAlarmIdx(),
                    null,
                    listenAlarms.get(i).getUser().getNickName()
            );
            newAlarms.add(alarm);
        }
        return newAlarms;
    }

    private Map<String, ProgramMainResponse> notLogin() {
        log.info("로그인 하지 않은 사용자 메인 페이지 조회");
        List<Center> favCenters = centerRepository.findTop4ByOrderByFavCountDesc();
        Map<String, ProgramMainResponse> programDatas = getProgramResponses(favCenters);
        return programDatas;
    }

    private Map<String, ProgramMainResponse> getProgramResponses(List<Center> favCenters) {
        Map<String, ProgramMainResponse> programDatas = new HashMap<>();
        log.info("size = {}", favCenters.size());

        for (int i = 0; i < favCenters.size(); i++) {
            List<getPrograms> mainResponses = new ArrayList<>();
            String region = favCenters.get(i).getRegion();

            LocalDate now = LocalDate.now();
            List<Program> top3Program = programRepository.findTop3ByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(now, now, region);

            for (int j = 0; j < top3Program.size(); j++) {
                log.info("topProgram = {}" + top3Program.get(j).getProgramName());
                mainResponses.add(getPrograms.from(top3Program.get(j)));
            }

            int favCount = favCenters.get(i).getFavCount();

            ProgramMainResponse programMainResponse = ProgramMainResponse.from(favCount, mainResponses);
            programDatas.put(favCenters.get(i).getRegion(), programMainResponse);
        }

        return programDatas;
    }
}
