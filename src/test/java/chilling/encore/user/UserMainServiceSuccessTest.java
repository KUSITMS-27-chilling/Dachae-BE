package chilling.encore.user;

import chilling.encore.domain.Center;
import chilling.encore.domain.User;
import chilling.encore.dto.UserDto.LearningInfo;
import chilling.encore.dto.UserDto.UserFavRegion;
import chilling.encore.dto.UserDto.UserGrade;
import chilling.encore.dto.UserDto.UserRegion;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.CenterRepository;
import chilling.encore.service.user.UserMainService;
import chilling.encore.utils.MockList;
import chilling.encore.utils.domain.MockCenter;
import chilling.encore.utils.MockLearningInfo;
import chilling.encore.utils.domain.MockUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserMainServiceSuccessTest {
    @Mock
    private CenterRepository centerRepository;
    @Mock
    private RedisRepository redisRepository;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private UserMainService userMainService;
    private MockUser user = new MockUser();
    private MockList mockList = new MockList(user);
    private List<Center> fourCenters = mockList.getCenters();
    @Test
    void getGradeTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));

        UserGrade grade = userMainService.getGrade();
        List<String> favField = grade.getFavField();
        String[] favFields = user.getFavField().split(",");

        assertThat(grade.getGrade()).isEqualTo(user.getGrade());
        assertThat(grade.getProfile()).isEqualTo(user.getProfile());
        assertThat(grade.getNickName()).isEqualTo(user.getNickName());

        for (int i = 0; i < favField.size(); i++) {
            assertThat(favField.get(i)).isEqualTo(favFields[i]);
        }
    }

    @Test
    void getRegionTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));

        UserRegion region = userMainService.getRegion();

        assertThat(region.getRegion()).isEqualTo(user.getRegion());
    }

    @Test
    void getOnlyFavRegionTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));

        UserFavRegion userFavRegion = userMainService.onlyFavRegion();
        List<String> regions = userFavRegion.getRegions();
        String[] favRegions = user.getFavRegion().split(",");

        for (int i = 0; i < regions.size(); i++) {
            assertThat(regions.get(i)).isEqualTo(favRegions[i]);
        }
    }

    @Test
    void loginFavCenterTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));

        UserFavRegion favCenter = userMainService.getFavCenter();
        List<String> regions = favCenter.getRegions();
        String[] favRegions = user.getFavRegion().split(",");

        assertThat(regions.get(0)).isEqualTo(user.getRegion());
        for (int i = 1; i < regions.size(); i++) {
            assertThat(regions.get(i)).isEqualTo(favRegions[i-1]);
        }
    }

    @Test
    void notLoginFavCenterTest() {
        given(securityUtils.getLoggedInUser()).willThrow(ClassCastException.class);
        given(centerRepository.findTop4ByOrderByFavCountDesc()).willReturn(fourCenters);

        UserFavRegion favCenter = userMainService.getFavCenter();
        List<String> regions = favCenter.getRegions();

        for (int i = 0; i < fourCenters.size(); i++) {
            assertThat(regions.get(i)).isEqualTo(fourCenters.get(i).getRegion());
        }
    }

    @Test
    void getLearningInfoTest() {
        MockLearningInfo mockLearningInfo = new MockLearningInfo();
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));
        given(redisRepository.getLearningInfoIds(user.getUserIdx() + "LearningInfo", 0, -1))
                .willReturn(mockLearningInfo.getIds());
        given(redisRepository.getLearningInfos(user.getUserIdx() + "LearningInfo", mockLearningInfo.getIds()))
                .willReturn(mockLearningInfo.getInfos());

        List<LearningInfo> learningInfo = userMainService.getLearningInfo();
        List<String> infos = mockLearningInfo.getInfos();
        for (int i = 0; i < learningInfo.size(); i++) {
            String[] learningInfos = infos.get(i).split(":");

            assertThat(learningInfo.get(learningInfo.size() - 1 - i).getIsFin())
                    .isEqualTo(learningInfos[0]);
            assertThat(learningInfo.get(learningInfo.size() - 1 - i).getListenIdx())
                    .isEqualTo(learningInfos[1]);
            assertThat(learningInfo.get(learningInfo.size() - 1 - i).getTitle())
                    .isEqualTo(learningInfos[2]);
            assertThat(learningInfo.get(learningInfo.size() - 1 - i).getNickName())
                    .isEqualTo(learningInfos[3]);
        }
    }
}
