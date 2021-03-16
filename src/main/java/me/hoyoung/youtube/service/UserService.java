package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.user.VideoListResponse;
import me.hoyoung.youtube.service.drive.DiskDrive;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final DiskDrive profileDrive;

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
    public List<VideoListResponse> findVideoList(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<VideoListResponse> vlist = userRepository.findVideos(user.getUuid());
        return vlist;
    }

    @Transactional
    public void updateProfileImage(MultipartFile File) throws IOException {
        String filename = profileDrive.createFile(File);
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid();
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setProfileImage(filename);
    }

    @Transactional
    public File getProfileImage() throws IOException {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid();
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return profileDrive.getFile(user.getProfileImage());
    }

    @Transactional
    public File getProfileImageById(Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return profileDrive.getFile(user.getProfileImage());
    }

    public File getProfileImageByFileName(String filename) throws IOException {
        return profileDrive.getFile(filename);
    }
}
