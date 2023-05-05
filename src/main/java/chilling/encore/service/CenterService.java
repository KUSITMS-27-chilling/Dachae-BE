package chilling.encore.service;

import chilling.encore.domain.Center;
import chilling.encore.dto.CenterDto;
import chilling.encore.dto.CenterDto.CenterInfo;
import chilling.encore.repository.springDataJpa.CenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CenterService {
    private final CenterRepository centerRepository;

    public CenterInfo getCenterInfo(String region) {
        Center center = centerRepository.findByRegion(region);
        CenterInfo centerInfo = CenterInfo.from(center);
        return centerInfo;
    }
}
