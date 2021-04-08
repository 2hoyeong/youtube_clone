package me.hoyoung.youtube.domain.comment;

import me.hoyoung.youtube.domain.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c.cuid as cuid, c.content as content, c.createdDate as createdDate, u.name as name FROM Comment c LEFT JOIN User u ON c.author = u.uuid WHERE c.video = :video")
    List<CommentListDao> findAllComment(@Param("video") Video video);
}

