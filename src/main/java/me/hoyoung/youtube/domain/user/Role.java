package me.hoyoung.youtube.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "비로그인"),
    USER("ROLE_USER", "로그인");

    private final String key;
    private final String value;
}
