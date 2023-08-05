package chilling.encore.service.freeBoard;

import chilling.encore.domain.Center;
import chilling.encore.domain.FreeBoard;
import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.User;
import chilling.encore.dto.FreeBoardDto;
import chilling.encore.dto.FreeBoardDto.*;
import chilling.encore.exception.FreeException;
import chilling.encore.exception.FreeException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.repository.springDataJpa.FreeBoardRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FreeBoardService {
    private final FreeBoardRepository freeBoardRepository;
    private final CenterRepository centerRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final int FREE_BOARD_PAGE_SIZE = 8;

    public AllFreeBoards getFreeBoardPage(Integer page, String region, String orderBy) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, FREE_BOARD_PAGE_SIZE);

        Page<FreeBoard> freeBoardPage = freeBoardRepository.findRegionFreeBoard(region, orderBy, pageable);
        List<SelectFreeBoard> selectFreeBoards = new ArrayList<>();
        for (int i = 0; i < freeBoardPage.getContent().size(); i++) {
            selectFreeBoards.add(SelectFreeBoard.from(freeBoardPage.getContent().get(i)));
        }
        return AllFreeBoards.builder()
                .freeBoards(selectFreeBoards)
                .build();
    }

    public PopularFreeBoards getPopular() {
        List<String> regions = new ArrayList<>();
        try {
            loginPopular(regions);
        } catch (ClassCastException e) {
            notLoginPopular(regions);
        }
        List<String> popularFreeBoard = freeBoardRepository.findPopularFreeBoard(regions);
        return PopularFreeBoards.builder().popularFreeBoards(popularFreeBoard).build();
    }

    private void notLoginPopular(List<String> regions) {
        List<Center> topRegions = centerRepository.findTop4ByOrderByFavCountDesc();
        for (int i = 0; i < topRegions.size(); i++) {
            regions.add(topRegions.get(i).getRegion());
        }
    }

    private void loginPopular(List<String> regions) {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));

        String[] favRegions = user.getFavRegion().split(",");
        for (int i = 0; i < favRegions.length; i++) {
            regions.add(favRegions[i]);
        }
    }

    public void save(CreateFreeBoardRequest createFreeBoardRequest) {
        User user = userRepository.findById(securityUtils.getLoggedInUser()
                .orElseThrow(() -> new ClassCastException("NotLogin"))
                .getUserIdx()).get();
        user.updateGrade();
        FreeBoard freeBoard = CreateFreeBoardRequest.to(createFreeBoardRequest, user);
        freeBoardRepository.save(freeBoard);
    }

    public FreeBoardDetail getDetail(Long freeBoardIdx) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardIdx).orElseThrow(() -> new NoSuchIdxException());
        return FreeBoardDetail.from(freeBoard);
    }
}
