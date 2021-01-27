package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Boolean signIn(UserSignInDto requestDto) {
        User user = userRepository.findById(requestDto.getId()).get();

        if (requestDto.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @Transactional
    public void signUp(UserSignUpDto requestDto) {
        userRepository.save(User.builder()
        .id(requestDto.getId())
        .password(requestDto.getPassword())
        .name(requestDto.getName()).build());
    }
}