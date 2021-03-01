package me.hoyoung.youtube.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class VideoResponseDto {
    private String id;
    private String uploader;
    private String thumbnailPath;
    private Timestamp createdDate;
}
