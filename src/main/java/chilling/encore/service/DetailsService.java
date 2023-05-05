package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import chilling.encore.dto.DetailDto.DetailResponse;
import chilling.encore.dto.ListenTogetherDto;
import chilling.encore.dto.ListenTogetherDto.SelectListenTogether;
import chilling.encore.dto.ProgramDto;
import chilling.encore.dto.ProgramDto.GetDetailPrograms;
import chilling.encore.dto.ProgramDto.PagingPrograms;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ParticipantsRepository;
import chilling.encore.repository.springDataJpa.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailsService {
    private final CenterRepository centerRepository;
    private final ProgramRepository programRepository;
    private final ListenTogetherRepository listenTogetherRepository;
    private final ParticipantsRepository participantsRepository;
    private final int PROGRAM_PAGE_SIZE = 6;
    private final int LISTEN_TOGETHER_PAGE_SIZE = 8;

    private final LocalDate now = LocalDate.now();

    public PagingPrograms getProgramPaging(String region, int page) {
        Page<Program> fullPrograms = getFullPrograms(region, page - 1);
        List<GetDetailPrograms> programs = getPrograms(fullPrograms);
        PagingPrograms pagingPrograms = PagingPrograms.from(page, programs);
        return pagingPrograms;
    }

    public DetailResponse firstDetails(String region) {
        Center center = centerRepository.findByRegion(region);
        int favCount = center.getFavCount();
        String tell = center.getTell();
        Page<Program> fullPrograms = getFullPrograms(region, 0);
        List<GetDetailPrograms> programs = getPrograms(fullPrograms);

        Page<ListenTogether> listenTogetherPage = getFullListenTogether(region, 0);
        List<SelectListenTogether> listenTogethers = getListenTogethers(listenTogetherPage);

        DetailResponse detailResponse =
                DetailResponse.from(favCount, tell, programs, listenTogethers, fullPrograms.getTotalPages(), listenTogetherPage.getTotalPages());

        return detailResponse;
    }

    private List<SelectListenTogether> getListenTogethers(Page<ListenTogether> listenTogetherPage) {
        List<SelectListenTogether> listenTogethers = new ArrayList<>();
        for (int i = 0; i < listenTogetherPage.getContent().size(); i++) {
            ListenTogether listenTogether = listenTogetherPage.getContent().get(i);
            User user = listenTogether.getUser();
            Program program = listenTogether.getProgram();
            int currentNum = participantsRepository.findAllByListenTogether_ListenIdx(listenTogether.getListenIdx()).size();
            boolean isRecruiting = true;
            if (program.getEndDate().isBefore(LocalDate.now()) || currentNum >= listenTogether.getGoalNum())
                isRecruiting = false;

            listenTogethers.add(SelectListenTogether.from(isRecruiting, user, listenTogether, currentNum, program));
        }
        return listenTogethers;
    }

    private Page<ListenTogether> getFullListenTogether(String region, int page) {
        Pageable pageable = PageRequest.of(page, LISTEN_TOGETHER_PAGE_SIZE, Sort.by("createdAt").descending());
        Page<ListenTogether> listenTogetherPage = listenTogetherRepository.findAllByProgram_LearningCenter_Region(region, pageable);
        return listenTogetherPage;
    }

    private Page<Program> getFullPrograms(String region, int page) {
        String startDate = "startDate";
        Pageable pageable = PageRequest.of(page, PROGRAM_PAGE_SIZE, Sort.by(startDate).descending());
        Page<Program> fullPrograms = programRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(
                now,
                now,
                region,
                pageable
        );
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
}
