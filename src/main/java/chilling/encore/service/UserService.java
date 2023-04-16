package chilling.encore.service;

import chilling.encore.domain.User;
import chilling.encore.dto.UserDto;
import chilling.encore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //회원가입
    public User signUp(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getId())
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .nickName(userDto.getNickName())
                .phoneNumber(userDto.getPhoneNumber())
                .status(0)
                .build();
        return userRepository.save(user);
    }

    public boolean checkIdDup(String id) {
        Optional<User> byUserId = userRepository.findByUserId(id);
        if (byUserId.isPresent())
            return false;
        return true;
    }

    public boolean checkNick(String nickName) {
        Optional<User> byNickName = userRepository.findByNickName(nickName);
        if (byNickName.isPresent())
            return false;
        return true;
    }
}
