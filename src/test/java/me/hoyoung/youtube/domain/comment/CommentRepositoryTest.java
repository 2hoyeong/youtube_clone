package me.hoyoung.youtube.domain.comment;

import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    private User mockUser;
    private Video mockVideo;

    @BeforeEach
    public void setupTest() {
        createMockUser();
        createMockVideo();
    }

    @AfterEach
    public void cleanUp() {
        commentRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void createMockUser() {
        String id = "testId";
        String password = "testPassword";
        String name = "테스트용 계정";
        mockUser = User.builder().id(id).name(name).password(password).build();
        userRepository.save(mockUser);
    }

    public void createMockVideo() {
        String originFileName = "oiiowjoiwjakl.mp4";
        String thumbnailPath = "zxcvjioipasd.png";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        mockVideo = Video.builder()
                .uploader(mockUser)
                .originalFileName(originFileName)
                .thumbnailPath(thumbnailPath)
                .createdDate(timestamp).build();
        videoRepository.save(mockVideo);
    }
}
