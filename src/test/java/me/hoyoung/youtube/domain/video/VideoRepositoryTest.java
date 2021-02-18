package me.hoyoung.youtube.domain.video;

import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void createMockUser() {
        String id = "testId";
        String password = "testPassword";
        String name = "테스트용 계정";
        mockUser = User.builder().id(id).name(name).password(password).build();
        userRepository.save(mockUser);
    }

    @AfterEach
    public void cleanUp() {
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("비디오 save 테스트")
    public void userSignUpTest() throws Exception {
        //given
        String originFileName = "oiiowjoiwjakl.mp4";
        String thumbnailPath = "zxcvjioipasd.png";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        videoRepository.save(Video.builder()
        .uploader(mockUser)
        .originalFileName(originFileName)
        .thumbnailPath(thumbnailPath)
        .createdDate(timestamp).build());

        //when
        List<Video> videoList = videoRepository.findAll();

        //then
        Video video = videoList.get(0);
        assertThat(video.getOriginalFileName()).isEqualTo(originFileName);
        assertThat(video.getThumbnailPath()).isEqualTo(thumbnailPath);
        assertThat(video.getCreatedDate()).isEqualTo(timestamp);
    }
}
