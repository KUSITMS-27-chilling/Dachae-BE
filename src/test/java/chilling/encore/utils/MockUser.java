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
            .favRegion("강남구,동작구")
            .favField("축구,컴퓨터")
            .profile("default")
            .build();
    public User getUser() {
        return mockUser;
    }
}
