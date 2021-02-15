package me.hoyoung.youtube.domain.video;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VideoDriveTest {

    @Autowired
    private VideoDrive videoDrive;

    @Value("${video.storage.dir}")
    private String directory;

    private String testExtension = ".tests";

    @BeforeEach
    public void createDir() {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @AfterEach
    public void cleanUp() {
        final File[] files = new File(directory).listFiles((dir, name) -> name.matches( ".+("+ testExtension +")$" ));
        Arrays.stream(files).forEach((file) -> file.delete());
    }

    @Test
    @DisplayName("비디오 createFile 테스트")
    public void userSignUpTest() throws Exception {
        //given
        String filename = UUID.randomUUID().toString() + testExtension;
        byte[] contents = "Test contents".getBytes();
        Path path = Paths.get(directory + filename);
        Files.write(path, contents);
        MultipartFile multipartFile = new MockMultipartFile(filename, filename, "text/plain",  new FileInputStream(new File(directory + filename)));

        //when
        String createdFileName = videoDrive.createFile(multipartFile);

        //then
        File createFile = new File(directory + createdFileName);
        assertThat(createFile.exists()).isTrue();
    }
}
