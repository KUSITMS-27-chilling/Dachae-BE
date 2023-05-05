package chilling.encore.service;


import chilling.encore.domain.Center;
import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import chilling.encore.dto.DetailDto;
import chilling.encore.dto.ListenTogetherDto;
import chilling.encore.dto.ListenTogetherDto.ListenTogetherPage;
import chilling.encore.dto.ListenTogetherDto.ListenTogetherResponse;
import chilling.encore.dto.ListenTogetherDto.PopularListenTogether;
import chilling.encore.dto.ListenTogetherDto.SelectListenTogether;
import chilling.encore.dto.ProgramDto;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListenTogetherService {
    private final ParticipantsRepository participantsRepository;
    private final ListenTogetherRepository listenTogetherRepository;

    private final int LISTEN_TOGETHER_PAGE_SIZE = 8;

    public ListenTogetherPage getListenTogetherPage(String region, Integer page) {
        if (page == null)
            page = 1;
        Page<ListenTogether> listenTogetherPage = getFullListenTogether(region, page-1);
        List<SelectListenTogether> listenTogethers = getListenTogethers(listenTogetherPage);
        return ListenTogetherPage.from(listenTogetherPage.getTotalPages(), listenTogethers);
    }

    private Page<ListenTogether> getFullListenTogether(String region, int page) {
        String[] regions = region.split(",");
        Pageable pageable = PageRequest.of(page, LISTEN_TOGETHER_PAGE_SIZE, Sort.by("createdAt").descending());
        log.info("regions = {}", regions[0]);
        Page<ListenTogether> listenTogetherPage = listenTogetherRepository.findRegionListenTogether(regions, pageable);
        return listenTogetherPage;
    }

    private List<SelectListenTogether> getListenTogethers(Page<ListenTogether> listenTogetherPage) {
        List<SelectListenTogether> listenTogethers = new ArrayList<>();
        for (int i = 0; i < listenTogetherPage.getContent().size(); i++) {
            ListenTogether listenTogether = listenTogetherPage.getContent().get(i);
            int currentNum = participantsRepository.findAllByListenTogether_ListenIdx(listenTogether.getListenIdx()).size();
            boolean isRecruiting = true;
            if (listenTogether.getProgram().getEndDate().isBefore(LocalDate.now()) || currentNum >= listenTogether.getGoalNum())
                isRecruiting = false;

            listenTogethers.add(SelectListenTogether.from(isRecruiting, listenTogether, currentNum));
        }
        return listenTogethers;
    }
}
