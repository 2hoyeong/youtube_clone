package me.hoyoung.youtube.domain.video;

import me.hoyoung.youtube.domain.user.VideoListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findById(String id);

    @Query("SELECT id as id, thumbnailPath as thumbnailPath, createdDate as createdDate, title as title FROM Video ORDER BY RAND()")
    List<VideoListResponse> findRandomVideo(Pageable pageable);
}
