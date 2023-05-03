package chilling.encore.service;


import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.User;
import chilling.encore.dto.ListenTogetherDto.ListenTogetherResponse;
import chilling.encore.dto.ListenTogetherDto.PopularListenTogether;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListenTogetherService {

    private final ListenTogetherRepository listenTogetherRepository;

    public ListenTogetherResponse getListenTogetherPage() {
        try {
            Optional<User> optionalUser = SecurityUtils.getLoggedInUser();
            User user = optionalUser.get();
            String region = user.getRegion();
            String favRegion = user.getFavRegion();
            String[] favRegions = favRegion.split(",");

            List<ListenTogether> mergeList = new ArrayList<>();
            for (String fav : favRegions) {
                mergeList.addAll(listenTogetherRepository.findTop3ByOrderByHitDesc(fav));
            }

            List<PopularListenTogether> popularListenTogethers = new ArrayList<>();
            for (ListenTogether listenTogether : mergeList) {
                popularListenTogethers.add(PopularListenTogether.from(listenTogether));
            }
            return ListenTogetherResponse.from(popularListenTogethers);

        } catch (ClassCastException e) {
            return ListenTogetherResponse.from(
                    null
            );
        }
    }
}
