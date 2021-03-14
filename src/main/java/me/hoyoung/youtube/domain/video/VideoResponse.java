package me.hoyoung.youtube.domain.video;

import java.sql.Timestamp;

public interface VideoResponse {
    String getId();
    String getThumbnailPath();
    Timestamp getCreatedDate();
    String getTitle();
    String getName();
    String getProfileImage();
    Long getViews();
}
