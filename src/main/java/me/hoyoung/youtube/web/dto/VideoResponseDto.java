package me.hoyoung.youtube.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.User;

import java.sql.Timestamp;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class VideoResponseDto {
    private String id;
    private User uploader;
    private String thumbnailPath;
    private Timestamp createdDate;
}
