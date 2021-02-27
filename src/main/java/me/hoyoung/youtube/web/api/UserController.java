package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.config.auth.JwtTokenProvider;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.VideoListResponse;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.service.UserService;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserTokenResponse;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider provider;

    @PostMapping("/api/v1/user/signin")
    public UserTokenResponse signIn(@RequestBody UserSignInDto requestDto) {
        User user = userService.signIn(requestDto);
        return new UserTokenResponse(provider.generateToken(user));
    }

    @PostMapping("/api/v1/user/signup")
    public void signUp(@RequestBody UserSignUpDto requestDto) {
        userService.signUp(requestDto);
    }


    @GetMapping("/api/v1/user/{userId}/videoList")
    public ResponseEntity<List<VideoListResponse>> getVideoListByUserId(@PathVariable("userId") String id) throws IOException {
        List<VideoListResponse> videoList = userService.findVideoList(id);
        return new ResponseEntity<>(videoList, HttpStatus.OK);
    }
}
