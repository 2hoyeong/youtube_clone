package me.hoyoung.youtube.domain.video;

import me.hoyoung.youtube.domain.user.VideoListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findById(String id);

    @Query("SELECT v.id as id, v.thumbnailPath as thumbnailPath, v.createdDate as createdDate, v.title as title, u.name as name, u.profileImage as profileImage, v.views as views FROM Video as v JOIN User as u ON u.uuid = v.uploader ORDER BY RAND()")
    List<VideoListResponse> findRandomVideo(Pageable pageable);

    @Query("SELECT v.id as id, v.thumbnailPath as thumbnailPath, v.createdDate as createdDate, v.title as title, u.name as name, u.profileImage as profileImage, v.views as views FROM Video as v JOIN User as u on u.uuid = v.uploader WHERE v.id = :id")
    VideoResponse findVideoInfo(@Param("id") String id);
}
