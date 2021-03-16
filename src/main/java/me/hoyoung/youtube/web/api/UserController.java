package me.hoyoung.youtube.web.api;

import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.config.auth.JwtTokenProvider;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.VideoListResponse;
import me.hoyoung.youtube.service.UserService;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserTokenResponse;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider provider;

    @PostMapping("/signin")
    public UserTokenResponse signIn(@RequestBody UserSignInDto requestDto) {
        User user = userService.signIn(requestDto);
        return new UserTokenResponse(provider.generateToken(user));
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody UserSignUpDto requestDto) {
        userService.signUp(requestDto);
    }


    @GetMapping("/{userId}/videoList")
    public ResponseEntity<List<VideoListResponse>> getVideoListByUserId(@PathVariable("userId") String id) {
        List<VideoListResponse> videoList = userService.findVideoList(id);
        return new ResponseEntity<>(videoList, HttpStatus.OK);
    }

    @PatchMapping("/profileImage")
    public void setProfileImage(@RequestPart("image") MultipartFile file) throws IOException {
        // TODO : 이미지 확장자 제한 추가
        userService.updateProfileImage(file);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<byte[]> getProfileImage() throws IOException {
        File profileImageFile = userService.getProfileImage();
        byte[] profileImageByteArray = Files.toByteArray(profileImageFile);
        return new ResponseEntity<>(profileImageByteArray, HttpStatus.OK);
    }

    @GetMapping("/{userId}/profileImage")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable("userId") Long id) throws IOException {
        File profileImageFile = userService.getProfileImageById(id);
        byte[] profileImageByteArray = Files.toByteArray(profileImageFile);
        return new ResponseEntity<>(profileImageByteArray, HttpStatus.OK);
    }

    @GetMapping("/profileImage/{filename}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable("filename")String filename) throws IOException {
        File profileImageFile = userService.getProfileImageByFileName(filename);
        byte[] profileImageByteArray = Files.toByteArray(profileImageFile);
        return new ResponseEntity<>(profileImageByteArray, HttpStatus.OK);
    }
}
