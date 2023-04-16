package chilling.encore.service;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public User signUp(UserDto userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickName(userDto.getNickName())
                .phoneNumber(userDto.getPhoneNumber())
                .status(0)
                .build();
        return userRepository.save(user);
    }
}
