package chilling.encore.service;


import chilling.encore.domain.*;
import chilling.encore.dto.DetailDto;
import chilling.encore.dto.ListenTogetherDto;
import chilling.encore.dto.ListenTogetherDto.*;
import chilling.encore.dto.ProgramDto;
import chilling.encore.global.config.security.util.SecurityUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListenTogetherService {
    private final ParticipantsRepository participantsRepository;
    private final ListenTogetherRepository listenTogetherRepository;
    private final ProgramRepository programRepository;
    private final CenterRepository centerRepository;

    private final int LISTEN_TOGETHER_PAGE_SIZE = 8;

    public ListenTogetherDetail getListenTogetherDetail(Long listenIdx) {
        ListenTogether listenTogether = listenTogetherRepository.findById(listenIdx).orElseThrow();
        List<Participants> participants = participantsRepository.findAllByListenTogether_ListenIdx(listenIdx);
        List<ParticipantsInfo> participantsInfos = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            ParticipantsInfo participantsInfo = ParticipantsInfo.from(participants.get(i).getUser());
            participantsInfos.add(participantsInfo);
        }

        return ListenTogetherDetail.from(listenTogether, participantsInfos);
    }

    public AllMyListenTogether getMyListenTogether() {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Long userIdx = user.getUserIdx();
        List<ListenTogether> listenTogethers = listenTogetherRepository.findTop3ByUser_UserIdxOrderByCreatedAtDesc(userIdx);
        List<MyListenTogether> myListenTogethers = new ArrayList<>();
        for (int i = 0; i < listenTogethers.size(); i++) {
            MyListenTogether myListenTogether = MyListenTogether.from(listenTogethers.get(i));
            myListenTogethers.add(myListenTogether);
        }
        return AllMyListenTogether.from(myListenTogethers);
    }

    public AllPopularListenTogether popularListenTogether() {
        List<String> regions = new ArrayList<>();
        try {
            return login(regions);
        } catch (ClassCastException e) {
            return notLogin(regions);
        }
    }

    private AllPopularListenTogether login(List<String> regions) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        regions.add(user.getRegion());
        if (user.getFavRegion() != null) {
            String[] favRegions = user.getFavRegion().split(",");
            for (int i = 0; i < favRegions.length; i++) {
                regions.add(favRegions[i]);
            }
        }
        return getPopularTitles(regions);
    }

    private AllPopularListenTogether notLogin(List<String> regions) {
        List<Center> centers = centerRepository.findTop4ByOrderByFavCountDesc();
        for (int i = 0; i < centers.size(); i++) {
            regions.add(centers.get(i).getRegion());
        }
        return getPopularTitles(regions);
    }

    private AllPopularListenTogether getPopularTitles(List<String> regions) {
        List<ListenTogether> listenTogethers = listenTogetherRepository.findPopularListenTogether(regions);
        List<PopularListenTogether> popularListenTogethers = new ArrayList<>();
        for (int i = 0; i < listenTogethers.size(); i++) {
            popularListenTogethers.add(PopularListenTogether.from(listenTogethers.get(i)));
        }
        return AllPopularListenTogether.from(popularListenTogethers);
    }

    public void save(CreateListenTogetherRequest createListenTogetherReq) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Program program = programRepository.findById(createListenTogetherReq.getProgramIdx()).orElseThrow();

        ListenTogether listenTogether = ListenTogether.builder()
                .goalNum(createListenTogetherReq.getGoalNum())
                .title(createListenTogetherReq.getTitle())
                .content(createListenTogetherReq.getContent())
                .hit(0)
                .program(program)
                .user(user)
                .build();
        listenTogetherRepository.save(listenTogether);
        return;
    }

    public ListenTogetherPage getListenTogetherPage(String region, Integer page) {
        if (page == null)
            page = 1;
        Page<ListenTogether> listenTogetherPage = getFullListenTogether(region, page-1);
        List<SelectListenTogether> listenTogethers = getListenTogethers(listenTogetherPage);
        return ListenTogetherPage.from(listenTogetherPage.getTotalPages(), listenTogethers);
    }

    private Page<ListenTogether> getFullListenTogether(String region, int page) {
        String[] regions = region.split(",");
        Pageable pageable = PageRequest.of(page, LISTEN_TOGETHER_PAGE_SIZE);
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
