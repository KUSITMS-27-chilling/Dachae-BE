package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.domain.Program;
import chilling.encore.dto.MainDto.DetailResponse;
import chilling.encore.dto.ProgramDto;
import chilling.encore.dto.ProgramDto.GetDetailPrograms;
import chilling.encore.repository.CenterRepository;
import chilling.encore.repository.ProgramRepository;
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

    private final LocalDate now = LocalDate.now();

    public DetailResponse getDetails(String region, Integer page) {
        if (page == null)
            page = 1;
        Center center = centerRepository.findByRegion(region);
        int favCount = center.getFavCount();
        String tell = center.getTell();
        String startDate = "startDate";
        Pageable pageable = PageRequest.of(page-1, 8, Sort.by(startDate).descending());
        Page<Program> fullPrograms = programRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndLearningCenter_Region(
                now,
                now,
                region,
                pageable
        );
        List<GetDetailPrograms> programs = new ArrayList<>();
        for (int i = 0; i < fullPrograms.getContent().size(); i++) {
            log.info("startDate = {}", fullPrograms.getContent().get(i).getStartDate());
            programs.add(GetDetailPrograms.from(fullPrograms.getContent().get(i)));
        }
        DetailResponse detailResponse = DetailResponse.from(favCount, tell, programs);
        return detailResponse;
    }
}
