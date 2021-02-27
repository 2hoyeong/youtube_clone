package me.hoyoung.youtube.domain.user;

import java.sql.Timestamp;

public interface VideoListResponse {
    String getId();
    String getThumbnilPath();
    Timestamp getCreatedDate();
}
