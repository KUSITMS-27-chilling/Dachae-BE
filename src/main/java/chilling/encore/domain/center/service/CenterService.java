package chilling.encore.domain.center.service;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.center.dto.CenterDto.CenterInfo;
import chilling.encore.domain.center.exception.CenterException.NoSuchRegionException;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class CenterService {
    private final CenterRepository centerRepository;

    public CenterInfo getCenterInfo(String region) {
        Center center = centerRepository.findByRegion(region).orElseThrow(() -> new NoSuchRegionException());
        CenterInfo centerInfo = CenterInfo.from(center);
        return centerInfo;
    }
}
