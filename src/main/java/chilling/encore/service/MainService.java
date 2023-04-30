package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import chilling.encore.dto.ProgramDto.ProgramMainResponse;
import chilling.encore.dto.ProgramDto.getPrograms;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.*;
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

    public Map<String, Object> getFirstPage() {
        try {
            Optional<User> optionalUser = SecurityUtils.getLoggedInUser();
            User user = optionalUser.get();
            String region = user.getRegion();
            String favRegion = user.getFavRegion();
            String[] favRegions = favRegion.split(",");
            return null;
        } catch (ClassCastException e) {
            return notLogin();
        }
    }

    private Map<String, Object> notLogin() {
        log.info("로그인 하지 않은 사용자 메인 페이지 조회");
        List<Center> favCenters = centerRepository.findTop4ByOrderByFavCountDesc();
        Map<String, Object> programDatas = getProgramResponses(favCenters);
        return programDatas;
    }

    private Map<String, Object> getProgramResponses(List<Center> favCenters) {
        Map<String, Object> programDatas = new HashMap<>();
        log.info("size = {}", favCenters.size());

        for (int i = 0; i < favCenters.size(); i++) {
            List<getPrograms> mainResponses = new ArrayList<>();
            String region = favCenters.get(i).getRegion();

            LocalDate now = LocalDate.now();
            List<Program> top3Program = programRepository.findTop3ByStartDateBeforeAndEndDateAfterAndLearningCenter_RegionOrderByStartDateDesc(now, now, region);

            for (int j = 0; j < top3Program.size(); j++) {
                log.info("topProgram = {}" + top3Program.get(j).getProgramName());
                mainResponses.add(getPrograms.from(top3Program.get(j)));
            }

            int favCount = centerRepository.findByRegion(region).getFavCount();

            ProgramMainResponse programMainResponse = ProgramMainResponse.from(favCount, mainResponses);
            programDatas.put(favCenters.get(i).getRegion(), programMainResponse);
        }

        return programDatas;
    }
}
