package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.service.UserService;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/user/signin")
    public Boolean signIn(@RequestBody UserSignInDto requestDto) {
        return userService.signIn(requestDto);
    }

}
