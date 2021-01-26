package me.hoyoung.youtube.web.dto;

import lombok.Getter;
import me.hoyoung.youtube.domain.user.User;

@Getter
public class UserSignUpDto {
    private String id;
    private String name;
    private String password;

    public UserSignUpDto (User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.password = entity.getPassword();
    }
}