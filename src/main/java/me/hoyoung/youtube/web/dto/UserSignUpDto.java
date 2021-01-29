package me.hoyoung.youtube.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoyoung.youtube.domain.user.User;

@Getter
@NoArgsConstructor
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
