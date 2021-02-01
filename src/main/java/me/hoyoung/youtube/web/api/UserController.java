package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.config.auth.JwtTokenProvider;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.service.UserService;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserTokenResponse;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
