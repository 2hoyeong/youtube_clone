package me.hoyoung.youtube.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    @Query("SELECT id as id, thumbnailPath as thumbnailPath, createdDate as createdDate FROM Video WHERE uploader.uuid = :id")
    List<VideoListResponse> findVideos(@Param("id") Long id);
}
