package me.hoyoung.youtube.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoyoung.youtube.domain.user.User;

@Getter
@NoArgsConstructor
public class UserSignInDto {
    private String id;
    private String password;

    public UserSignInDto (User entity) {
        this.id = entity.getId();
        this.password = entity.getPassword();
    }

}
