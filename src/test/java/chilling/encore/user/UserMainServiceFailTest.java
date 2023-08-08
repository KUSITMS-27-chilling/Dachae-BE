package chilling.encore.user;

import chilling.encore.domain.Center;
import chilling.encore.domain.User;
import chilling.encore.exception.UserException.NoSuchIdxException;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.UserRepository;
import chilling.encore.service.user.UserMainService;
import chilling.encore.utils.domain.MockCenter;
import chilling.encore.utils.domain.MockUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserMainServiceFailTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private UserMainService userMainService;

    @Test
    void invalidUserIdTest() {
        given(userRepository.findByUserId("testFail")).willReturn(Optional.empty());

        assertThatThrownBy(() -> userMainService.validateUserId("testFail"))
                .isInstanceOf(NoSuchIdxException.class);
    }

    @Test
    void getGradeFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.empty());

        assertThatThrownBy(() -> userMainService.getGrade())
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void getRegionFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.empty());

        assertThatThrownBy(() -> userMainService.getRegion())
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void getOnlyFavRegionFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.empty());

        assertThatThrownBy(() -> userMainService.onlyFavRegion())
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void getLearningInfoFailTest() {
        given(securityUtils.getLoggedInUser()).willReturn(Optional.empty());

        assertThatThrownBy(() -> userMainService.getLearningInfo())
                .isInstanceOf(ClassCastException.class);
    }
}
