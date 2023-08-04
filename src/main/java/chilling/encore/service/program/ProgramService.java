package chilling.encore.service.program;


import chilling.encore.domain.*;
import chilling.encore.dto.ProgramDto;
import chilling.encore.dto.ProgramDto.*;
import chilling.encore.exception.CenterException;
import chilling.encore.exception.CenterException.NoSuchRegionException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ProgramService {
    private final ProgramRepository programRepository;
    private final CenterRepository centerRepository;
    private SecurityUtils securityUtils = new SecurityUtils();

    private final int PROGRAM_PAGE_SIZE = 6;
    private final LocalDate now = LocalDate.now();

    public AllProgramInCenter getAllProgram(String region) {
        List<Program> allPrograms = programRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(now, now, region)
                .orElseThrow(() -> new NoSuchRegionException());
        List<GetProgramTitle> programTitles = new ArrayList<>();
        for (int i = 0; i < allPrograms.size(); i++)
            programTitles.add(GetProgramTitle.from(allPrograms.get(i)));
        return AllProgramInCenter.from(programTitles);
    }

    public PagingPrograms getPagingProgram(String region, int page) {
        Page<Program> fullPrograms = getFullPrograms(region, page - 1);
        List<GetDetailPrograms> programs = getPrograms(fullPrograms);
        PagingPrograms pagingPrograms = PagingPrograms.from(fullPrograms.getTotalPages(), programs);
        return pagingPrograms;
    }

    private Page<Program> getFullPrograms(String region, int page) {
        String startDate = "startDate";
        Pageable pageable = PageRequest.of(page, PROGRAM_PAGE_SIZE, Sort.by(startDate).descending());
        Page<Program> fullPrograms = programRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(
                now,
                now,
                region,
                pageable
        ).orElseThrow(() -> new NoSuchRegionException());
        return fullPrograms;
    }

    private List<GetDetailPrograms> getPrograms(Page<Program> fullPrograms) {
        List<GetDetailPrograms> programs = new ArrayList<>();
        for (int i = 0; i < fullPrograms.getContent().size(); i++) {
            log.info("startDate = {}", fullPrograms.getContent().get(i).getStartDate());
            programs.add(GetDetailPrograms.from(fullPrograms.getContent().get(i)));
        }
        return programs;
    }

    public AllProgramMainResponses getCenterPrograms() {
        try {
            User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
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
        favCenters.add(centerRepository.findByRegion(region).orElseThrow(() -> new NoSuchRegionException()));

        if (favRegion != null) {
            favRegions = favRegion.split(",");

            for (int i = 0; i < favRegions.length; i++) {
                Center center = centerRepository.findByRegion(favRegions[i]).orElseThrow(() -> new NoSuchRegionException());
                favCenters.add(center);
            }
        }

        Map<String, ProgramMainResponse> programDatas = getProgramResponses(favCenters);
        return programDatas;
    }

    private Map<String, ProgramMainResponse> getProgramResponses(List<Center> favCenters) {
        Map<String, ProgramMainResponse> programDatas = new LinkedHashMap<>();
        log.info("size = {}", favCenters.size());

        for (int i = 0; i < favCenters.size(); i++) {
            List<ProgramDto.getPrograms> mainResponses = new ArrayList<>();
            String region = favCenters.get(i).getRegion();

            LocalDate now = LocalDate.now();
            List<Program> top3Program = programRepository.findTop3ByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(now, now, region)
                    .orElseThrow(() -> new NoSuchRegionException());

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
            User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
            region = user.getRegion();
            return getNewProgramsResponse(region);
        }
        return getNewProgramsResponse(region);
    }

    private NewProgramsResponse getNewProgramsResponse(String region) {
        LocalDate thisMonth = LocalDate.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01");
        LocalDate nextMonth = LocalDate.parse(now.plusMonths(1L).format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01");
        List<Program> programs = programRepository.findTop10ByStartDateBetweenAndEndDateGreaterThanEqualAndLearningCenter_RegionOrderByStartDateDesc(thisMonth, nextMonth, now, region)
                .orElseThrow(() -> new NoSuchRegionException());
        List<ProgramDto.NewProgram> newPrograms = new ArrayList<>();
        for (int i = 0; i < programs.size(); i++) {
            newPrograms.add(ProgramDto.NewProgram.from(programs.get(i)));
        }
        return NewProgramsResponse.from(newPrograms);
    }
}
