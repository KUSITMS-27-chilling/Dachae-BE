package chilling.encore.utils;

import chilling.encore.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static chilling.encore.dto.responseMessage.UserConstants.Role.ROLE_USER;

public class MockUser {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private User mockUser = User.builder()
            .userIdx(10000L)
            .userId("forTest")
            .name("forTest")
            .gender("male")
            .age(100)
            .email("test.com")
            .password(bCryptPasswordEncoder.encode("1234"))
            .nickName("forTest")
            .phoneNumber("1234")
            .role(ROLE_USER)
            .status(0)
            .region("강서구")
            .profile("강남구")
            .build();
    public User getUser() {
        return mockUser;
    }
}
