package me.hoyoung.youtube.domain.comment;

import java.sql.Timestamp;

public interface CommentListDao {
    String getName();
    String getContent();
    Timestamp getCreatedDate();
    Long getCuid();
}

