package chilling.encore.service;

import chilling.encore.domain.FreeBoard;
import chilling.encore.domain.ListenTogether;
import chilling.encore.dto.FreeBoardDto;
import chilling.encore.dto.FreeBoardDto.AllFreeBoards;
import chilling.encore.dto.FreeBoardDto.SelectFreeBoard;
import chilling.encore.repository.springDataJpa.FreeBoardRepository;
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
}
