package chilling.encore.service.center;

import chilling.encore.domain.Center;
import chilling.encore.dto.CenterDto.CenterInfo;
import chilling.encore.exception.CenterException;
import chilling.encore.exception.CenterException.NoSuchRegionException;
import chilling.encore.repository.springDataJpa.CenterRepository;
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
