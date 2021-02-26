package me.hoyoung.youtube.domain.user;

import me.hoyoung.youtube.domain.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    @Query("SELECT id, thumbnailPath, createdDate FROM Video WHERE uploader.id = ?1")
    List<Video> findVideos(String id);
}
