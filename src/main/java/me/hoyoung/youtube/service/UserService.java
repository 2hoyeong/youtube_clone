package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User signIn(UserSignInDto requestDto) {
        User user = userRepository.findById(requestDto.getId()).orElseThrow(() ->  new UsernameNotFoundException("아이디와 비밀번호를 확인해주세요."));

        if (!requestDto.getPassword().equals(user.getPassword())) {
            new UsernameNotFoundException("아이디와 비밀번호를 확인해주세요.");
        }

        return user;
    }

    @Transactional
    public void signUp(UserSignUpDto requestDto) {
        if (checkUserExistById(requestDto.getId())) {
            throw new IllegalArgumentException("이미 회원가입 된 아이디 입니다.");
        }

        userRepository.save(User.builder()
        .id(requestDto.getId())
        .password(requestDto.getPassword())
        .name(requestDto.getName()).build());
    }

    @Transactional
    public boolean checkUserExistById(String id) throws UsernameNotFoundException{
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        return true;
    }

    @Transactional
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return userDetails;
    }

    @Transactional
    public List<Video> findVideoList(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return userRepository.findVideos(user.getId());
    }
}
