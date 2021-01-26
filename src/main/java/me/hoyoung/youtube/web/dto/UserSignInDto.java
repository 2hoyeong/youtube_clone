package me.hoyoung.youtube.web.dto;

import lombok.Getter;
import me.hoyoung.youtube.domain.user.User;

@Getter
public class UserSignInDto {
    private String id;
    private String password;

    public UserSignInDto (User entity) {
        this.id = entity.getId();
        this.password = entity.getPassword();
    }

}
