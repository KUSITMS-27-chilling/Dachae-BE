package chilling.encore.user;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto.*;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.UserRepository;
import chilling.encore.service.user.UserMyPageService;
import chilling.encore.utils.MockList;
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
public class UserMyPageServiceSuccessTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private UserMyPageService userMyPageService;
    private User user;
    private MockUser beforeUser;
    private MockList mockList;
    @BeforeEach
    void setUp() {
        beforeUser = new MockUser();
        mockList = new MockList(beforeUser);
        beforeUser.setParticipants(mockList.getParticipants());
        beforeUser.setListenTogethers(mockList.getListenTogethers());
        beforeUser.setLectureMessages(mockList.getLectureMessages());
        beforeUser.setReviews(mockList.getReviews());
        user = beforeUser;
        given(securityUtils.getLoggedInUser()).willReturn(Optional.of(user));
    }

    @Test
    void editFavRegion() {
        given(userRepository.findById(user.getUserIdx())).willReturn(Optional.of(user));
        EditFavRegion favRegion =
                EditFavRegion.builder()
                .favRegion("동작구")
                .build();

        userMyPageService.editFavRegion(favRegion);
        assertThat(user.getFavRegion()).isEqualTo(favRegion.getFavRegion());
    }

    @Test
    void editNickName() {
        given(userRepository.findById(user.getUserIdx())).willReturn(Optional.of(user));
        EditNickName editNickName =
                EditNickName.builder()
                        .nickName("editNick")
                        .build();

        userMyPageService.editNickName(editNickName);
        assertThat(user.getNickName()).isEqualTo(editNickName.getNickName());
    }

    @Test
    void editFavField() {
        given(userRepository.findById(user.getUserIdx())).willReturn(Optional.of(user));
        EditFavField editFavField =
                EditFavField.builder()
                        .favField("컴퓨터")
                        .build();

        userMyPageService.editFavField(editFavField);
        assertThat(user.getFavField()).isEqualTo(editFavField.getFavField());
    }

    @Test
    void getUserInfo() {
        List<String> favRegions = new ArrayList<>();
        favRegions.add(user.getRegion());
        for (String favRegion : user.getFavRegion().split(","))
            favRegions.add(favRegion);
        List<String> favFields = new ArrayList<>();
        for (String favField : user.getFavField().split(","))
            favFields.add(favField);

        UserInfo userInfo = userMyPageService.getUserInfo();

        assertThat(userInfo.getProfile()).isEqualTo(user.getProfile());
        assertThat(userInfo.getGrade()).isEqualTo(user.getGrade());
        assertThat(userInfo.getNickName()).isEqualTo(user.getNickName());
        assertThat(userInfo.getFavRegion()).isEqualTo(favRegions);
        assertThat(userInfo.getFavFiled()).isEqualTo(favFields);
    }

    @Test
    void getTotalParticipants() {
        given(userRepository.findById(user.getUserIdx())).willReturn(Optional.of(user));

        GetTotalParticipants userParticipant = userMyPageService.getUserParticipant();

        assertThat(userParticipant.getParticipantTotal()).isEqualTo(user.getParticipants().size());
        assertThat(userParticipant.getApplyLectureTotal()).isEqualTo(user.getLectureMessages().size());
    }

    @Test
    void getTotalWriteTotal() {
        given(userRepository.findById(user.getUserIdx())).willReturn(Optional.of(user));

        GetTotalWrite totalWrite = userMyPageService.getTotalWrite();

        assertThat(totalWrite.getFreeTotal()).isEqualTo(user.getFreeBoards().size());
        assertThat(totalWrite.getLectureTotal()).isEqualTo(0);
        assertThat(totalWrite.getListenTotal()).isEqualTo(user.getListenTogethers().size());
        assertThat(totalWrite.getReviewTotal()).isEqualTo(user.getReviews().size());
        assertThat(totalWrite.getAllTotal()).isEqualTo(user.getListenTogethers().size() + user.getReviews().size());
    }
}
