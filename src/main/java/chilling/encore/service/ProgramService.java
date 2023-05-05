package chilling.encore.service;


import chilling.encore.domain.*;
import chilling.encore.dto.ProgramDto;
import chilling.encore.dto.ProgramDto.AllProgramMainResponses;
import chilling.encore.dto.ProgramDto.NewProgramsResponse;
import chilling.encore.dto.ProgramDto.PagingPrograms;
import chilling.encore.dto.ProgramDto.ProgramMainResponse;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final CenterRepository centerRepository;

    private LocalDate yesterDay = LocalDate.now().minusDays(1);

//    public PagingPrograms getPagingPrograms(String region) {
//        Page<Program> programs = programRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region();
//    }

    public AllProgramMainResponses getCenterPrograms() {
        try {
            User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
            return AllProgramMainResponses.from(loginUser(user));
        } catch (ClassCastException e) {
            log.info("로그인 하지 않은 사용자 메인 페이지 조회");
            List<Center> favCenters = centerRepository.findTop4ByOrderByFavCountDesc();
            return AllProgramMainResponses.from(getProgramResponses(favCenters));
        }
    }

    private Map<String, ProgramMainResponse> loginUser(User user) {
        String region = user.getRegion();
        String favRegion = user.getFavRegion();
        return getProgramDatas(region, favRegion);
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

    private Map<String, ProgramMainResponse> getProgramResponses(List<Center> favCenters) {
        Map<String, ProgramMainResponse> programDatas = new HashMap<>();
        log.info("size = {}", favCenters.size());

        for (int i = 0; i < favCenters.size(); i++) {
            List<ProgramDto.getPrograms> mainResponses = new ArrayList<>();
            String region = favCenters.get(i).getRegion();

            LocalDate now = LocalDate.now();
            List<Program> top3Program = programRepository.findTop3ByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(now, now, region);

            for (int j = 0; j < top3Program.size(); j++) {
                log.info("topProgram = {}" + top3Program.get(j).getProgramName());
                mainResponses.add(ProgramDto.getPrograms.from(top3Program.get(j)));
            }

            int favCount = favCenters.get(i).getFavCount();

            ProgramMainResponse programMainResponse = ProgramMainResponse.from(favCount, mainResponses);
            programDatas.put(favCenters.get(i).getRegion(), programMainResponse);
        }

        return programDatas;
    }

    public NewProgramsResponse getNewPrograms(String region) {
        if(region == null) {
            User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
            region = user.getRegion();
            return getNewProgramsResponse(region);
        }
        return getNewProgramsResponse(region);
    }

    private NewProgramsResponse getNewProgramsResponse(String region) {
        List<Program> programs = programRepository.findAllByStartDateGreaterThanEqualAndLearningCenter_Region(yesterDay, region);
        List<ProgramDto.NewProgram> newPrograms = new ArrayList<>();
        for (int i = 0; i < programs.size(); i++) {
            newPrograms.add(ProgramDto.NewProgram.from(programs.get(i)));
        }
        return NewProgramsResponse.from(newPrograms);
    }
}
