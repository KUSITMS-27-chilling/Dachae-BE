package chilling.encore.domain.listenTogether.service;


import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.listenTogether.dto.ListenTogetherDto.*;
import chilling.encore.domain.listenTogether.exception.ListenException.NoSuchIdxException;
import chilling.encore.domain.program.exception.ProgramException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.listenTogether.repository.jpa.ListenTogetherRepository;
import chilling.encore.domain.program.repository.jpa.ProgramRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListenTogetherService {
    private final ListenTogetherRepository listenTogetherRepository;
    private final ProgramRepository programRepository;
    private final CenterRepository centerRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final int LISTEN_TOGETHER_PAGE_SIZE = 8;

    public ListenTogetherDetail getListenTogetherDetail(Long listenIdx) {
        ListenTogether listenTogether = listenTogetherRepository.findById(listenIdx)
                .orElseThrow(() -> new NoSuchIdxException());
        listenTogether.upHit();
        List<Participants> participants = listenTogether.getParticipants();
        List<ParticipantsInfo> participantsInfos = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            ParticipantsInfo participantsInfo = ParticipantsInfo.from(participants.get(i).getUser());
            participantsInfos.add(participantsInfo);
        }

        return ListenTogetherDetail.from(listenTogether, participantsInfos);
    }

    public AllMyListenTogether getMyListenTogether() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        Long userIdx = user.getUserIdx();
        List<ListenTogether> listenTogethers = listenTogetherRepository.findTop3ByUser_UserIdxOrderByHitDesc(userIdx);
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
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        regions.add(user.getRegion());
        if (user.getFavRegion() != null) {
            for (String favRegion : user.getFavRegion().split(",")) {
                regions.add(favRegion);
            }
        }
        return getPopularTitles(regions);
    }

    private AllPopularListenTogether notLogin(List<String> regions) {
        List<Center> centers = centerRepository.findTop4ByOrderByFavCountDesc();
        for (Center center : centers) {
            regions.add(center.getRegion());
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
        User user = userRepository.findById(securityUtils.getLoggedInUser()
                .orElseThrow(() -> new ClassCastException("NotLogin"))
                .getUserIdx()).get();
        Program program = programRepository.findById(createListenTogetherReq.getProgramIdx()).orElseThrow(() -> new ProgramException.NoSuchIdxException());
        ListenTogether listenTogether = ListenTogether.builder()
                .goalNum(createListenTogetherReq.getGoalNum())
                .title(createListenTogetherReq.getTitle())
                .content(createListenTogetherReq.getContent())
                .hit(0)
                .program(program)
                .user(user)
                .build();
        listenTogetherRepository.save(listenTogether);
        user.updateGrade();
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
        Page<ListenTogether> listenTogetherPage = listenTogetherRepository.findRegionListenTogether(regions, pageable);
        return listenTogetherPage;
    }

    private List<SelectListenTogether> getListenTogethers(Page<ListenTogether> listenTogetherPage) {
        List<SelectListenTogether> listenTogethers = new ArrayList<>();
        for (int i = 0; i < listenTogetherPage.getContent().size(); i++) {
            ListenTogether listenTogether = listenTogetherPage.getContent().get(i);
            int currentNum = listenTogether.getParticipants().size();
            boolean isRecruiting = true;
            if (listenTogether.getProgram().getEndDate().isBefore(LocalDate.now()) || currentNum >= listenTogether.getGoalNum())
                isRecruiting = false;

            listenTogethers.add(SelectListenTogether.from(isRecruiting, listenTogether, currentNum));
        }
        return listenTogethers;
    }
}
