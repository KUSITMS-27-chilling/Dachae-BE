package chilling.encore.service;


import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.User;
import chilling.encore.dto.ListenTogetherDto;
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


/*
            List<ListenTogetherPosts> listenTogetherPosts = new ArrayList<>();
            List<ListenTogether> topByOrderByHitsDesc = listenTogetherRepository.findTopByOrderByHitDesc();
            for (ListenTogether listenTogether : topByOrderByHitsDesc) {
                listenTogetherPosts.add(ListenTogetherPosts.from(listenTogether));
            }
            PopularListenPostsResponse popularListenPostsResponse = PopularListenPostsResponse.from(listenTogetherPosts);
*/

            List<ListenTogether> mergeList = new ArrayList<>();
            for (String fav : favRegions) {
                mergeList.addAll(listenTogetherRepository.findTop3ByOrderByHitDesc(fav));
            }

            List<PopularListenTogether> popularListenTogethers = new ArrayList<>();
            for (ListenTogether listenTogether : mergeList) {
                popularListenTogethers.add(PopularListenTogether.from(listenTogether));
            }
            return ListenTogetherResponse.from(popularListenTogethers);
//            return ListenTogetherResponse.from(popularListenPostsResponse, listenTogetherPosts);
/*
            List<ListenTogether> top3ListenTogethers = new ArrayList<>();
            List<ListenTogether> listenTogethers = new ArrayList<>();

            for (String fav : favRegions) {
                top3ListenTogethers.add(listenTogetherRepository.findTopByOrderByHitsDesc(fav)); // 지역의 인기글
                ListenTogetherPosts.from(listenTogetherRepository.findTopByOrderByHitsDesc(fav));

                listenTogethers.add(listenTogetherRepository.findAllByRegion(fav)); // 지역의 모든 글

                List<ListenTogetherPosts> listenTogetherPosts = new ArrayList<>();
                for (ListenTogether listenTogether : listenTogethers) {
                    listenTogetherPosts.add(ListenTogetherPosts.from(listenTogether));
                }

                PopularListenPostsResponse popularListenPostsResponse = PopularListenPostsResponse.from(listenTogetherPosts);
                listenTogetherResponse = ListenTogetherResponse.from(popularListenPostsResponse, listenTogetherPosts);
            }
*/
        } catch (ClassCastException e) {
            return ListenTogetherResponse.from(
                    null
            );
        }
    }
}
