package me.hoyoung.youtube.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentAddDto {
    private String content;

    public CommentAddDto(String content) {
        this.content = content;
    }
}
