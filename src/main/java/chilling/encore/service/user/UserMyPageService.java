package chilling.encore.service.user;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserMyPageService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    public void editFavRegion(UserDto.EditFavRegion favRegion) {
        User securityUser = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateFavRegion(favRegion.getFavRegion());
    }

    public void editNickName(UserDto.EditNickName editNickName) {
        User securityUser = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateNickName(editNickName.getNickName());
    }

    public void editFavField(UserDto.EditFavField editFavField) {
        User securityUser = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        User user = userRepository.findById(securityUser.getUserIdx()).get();
        user.updateFavField(editFavField.getFavField());
    }

    public UserDto.UserInfo getUserInfo() {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        return UserDto.UserInfo.from(user);
    }

    public UserDto.GetTotalParticipants getUserParticipant() {
        User user = userRepository.findById(
                securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        int participantsTotal = user.getParticipants().size();
        int applyLectureTotal = user.getLectureMessages().size();

        return UserDto.GetTotalParticipants.from(participantsTotal, applyLectureTotal);
    }

    public UserDto.GetTotalWrite getTotalWrite() {
        User user = userRepository.findById(
                securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"))
                        .getUserIdx()).orElseThrow();
        int listenTotal = user.getListenTogethers().size();
        int reviewTotal = user.getReviews().size();
        int lectureTotal;
        try {
            lectureTotal = user.getTeacherInfo().getLectures().size();
        } catch (NullPointerException e) {
            lectureTotal = 0;
        }
        int freeTotal = user.getFreeBoards().size();
        int allTotal = listenTotal + reviewTotal;

        return UserDto.GetTotalWrite.from(allTotal, listenTotal, reviewTotal, lectureTotal, freeTotal);
    }
}
