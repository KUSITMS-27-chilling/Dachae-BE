package chilling.encore.listenTogether;

import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.listenTogether.dto.ListenTogetherDto.*;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.center.repository.jpa.CenterRepository;
import chilling.encore.domain.listenTogether.repository.jpa.ListenTogetherRepository;
import chilling.encore.domain.program.repository.jpa.ProgramRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import chilling.encore.domain.listenTogether.service.ListenTogetherService;
import chilling.encore.utils.MockList;
import chilling.encore.utils.domain.MockListenTogether;
import chilling.encore.utils.domain.MockUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ListenTogetherServiceSuccessTest {
    @Mock
    ListenTogetherRepository listenTogetherRepository;
    @Mock
    ProgramRepository programRepository;
    @Mock
    CenterRepository centerRepository;
    @Mock
    SecurityUtils securityUtils;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ListenTogetherService listenTogetherService;

    private MockUser user;
    private MockList mockList;

    @BeforeEach
    void setUp() {
        user = new MockUser();
        mockList = new MockList(user);
        user.setParticipants(mockList.getParticipants());
        user.setListenTogethers(mockList.getListenTogethers());
        user.setLectureMessages(mockList.getLectureMessages());
        user.setReviews(mockList.getReviews());
    }

    @Test
    void getListenTogetherDetailTest() {
        MockListenTogether listenTogether = mockList.getListenTogether1();
        listenTogether.setParticipants(mockList.getParticipants());
        List<ParticipantsInfo> participantsInfos = getParticipantsInfos(listenTogether);
        ListenTogetherDetail result = ListenTogetherDetail.from(listenTogether, participantsInfos);

        given(listenTogetherRepository.findById(listenTogether.getListenIdx()))
                .willReturn(Optional.of(listenTogether));

        ListenTogetherDetail listenTogetherDetail = listenTogetherService.getListenTogetherDetail(listenTogether.getListenIdx());

        assertThat(listenTogetherDetail.getHit()).isEqualTo(result.getHit());
        assertThat(listenTogetherDetail.getProfile()).isEqualTo(result.getProfile());
        assertThat(listenTogetherDetail.getGrade()).isEqualTo(result.getGrade());
        assertThat(listenTogetherDetail.getTitle()).isEqualTo(result.getTitle());
        assertThat(listenTogetherDetail.getCreatedAt()).isEqualTo(result.getCreatedAt());
        assertThat(listenTogetherDetail.getCurrentNum()).isEqualTo(result.getCurrentNum());
        assertThat(listenTogetherDetail.getContent()).isEqualTo(result.getContent());
        assertThat(listenTogetherDetail.getGoalNum()).isEqualTo(result.getGoalNum());
        assertThat(listenTogetherDetail.getFavField().size()).isEqualTo(result.getFavField().size());
    }

    private List<ParticipantsInfo> getParticipantsInfos(MockListenTogether listenTogether) {
        List<ParticipantsInfo> participantsInfos = new ArrayList<>();
        for (Participants participants : listenTogether.getParticipants()) {
            participantsInfos.add(ParticipantsInfo.from(participants.getUser()));
        }
        return participantsInfos;
    }

    @Test
    void getMyListenTogether() {
        List<ListenTogether> listenTogethers = mockList.getListenTogethers();
        given(securityUtils.getLoggedInUser())
                .willReturn(Optional.of(user));
        given(listenTogetherRepository.findTop3ByUser_UserIdxOrderByHitDesc(user.getUserIdx()))
                .willReturn(listenTogethers);
        log.info("size = {}", mockList.getListenTogethers().size());

        List<MyListenTogether> myListenTogethers = listenTogetherService.getMyListenTogether().getMyListenTogethers();

        for (int i = 0; i < 3; i++) {
            assertThat(myListenTogethers.get(i).getListenIdx())
                    .isEqualTo(listenTogethers.get(i).getListenIdx());
            assertThat(myListenTogethers.get(i).getTitle())
                    .isEqualTo(listenTogethers.get(i).getTitle());
            assertThat(myListenTogethers.get(i).getCreatedAt())
                    .isEqualTo(listenTogethers.get(i).getCreatedAt());
        }
    }

    @Test
    void getLoginListenTogether() {
        given(securityUtils.getLoggedInUser())
                .willReturn(Optional.of(user));

        List<String> regions = getLoginRegions();
        List<PopularListenTogether> popularTitles = getPopularTitles(regions);

        for (int i = 0; i < popularTitles.size(); i++) {
            assertThat(popularTitles.get(i).getTitle())
                    .isEqualTo(mockList.getListenTogethers().get(i).getTitle());
        }
    }
    private List<String> getLoginRegions() {
        List<String> regions = new ArrayList<>();
        regions.add(user.getRegion());
        for (String favRegion : user.getFavRegion().split(","))
            regions.add(favRegion);
        return regions;
    }

    @Test
    void getNotLoginListenTogether() {
        given(securityUtils.getLoggedInUser())
                .willReturn(Optional.empty());
        given(centerRepository.findTop4ByOrderByFavCountDesc())
                .willReturn(mockList.getCenters());

        List<String> regions = getNotLoginRegions();
        List<PopularListenTogether> popularTitles = getPopularTitles(regions);

        for (int i = 0; i < popularTitles.size(); i++) {
            assertThat(popularTitles.get(i).getTitle())
                    .isEqualTo(mockList.getListenTogethers().get(i).getTitle());
        }
    }

    private List<String> getNotLoginRegions() {
        List<String> regions = new ArrayList<>();
        for (Center center : mockList.getCenters()) {
            regions.add(center.getRegion());
        }
        return regions;
    }

    private List<PopularListenTogether> getPopularTitles(List<String> regions) {
        given(listenTogetherRepository.findPopularListenTogether(regions))
                .willReturn(mockList.getListenTogethers());

        List<PopularListenTogether> popularListenTogethers =
                listenTogetherService.popularListenTogether()
                .getPopularListenTogethers();
        return popularListenTogethers;
    }

}
