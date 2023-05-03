package chilling.encore.service;


import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.User;
import chilling.encore.dto.ListenTogetherDto.ListenTogetherResponse;
import chilling.encore.dto.ListenTogetherPostDto;
import chilling.encore.dto.ListenTogetherPostDto.ListenTogetherPosts;
import chilling.encore.dto.ListenTogetherPostDto.PopularListenPostsResponse;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.ListenTogetherRepository;
import chilling.encore.repository.UserRepository;
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


            List<ListenTogetherPosts> listenTogetherPosts = new ArrayList<>();
            List<ListenTogether> topByOrderByHitsDesc = listenTogetherRepository.findTopByOrderByHitDesc();
            for (ListenTogether listenTogether : topByOrderByHitsDesc) {
                listenTogetherPosts.add(ListenTogetherPosts.from(listenTogether));
            }
            PopularListenPostsResponse popularListenPostsResponse = PopularListenPostsResponse.from(listenTogetherPosts);

            return ListenTogetherResponse.from(popularListenPostsResponse, listenTogetherPosts);
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
                    null,
                    null
            );
        }
    }
}
