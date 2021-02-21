package me.hoyoung.youtube.domain.video;

import me.hoyoung.youtube.util.MockFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VideoDriveTest {

    @Autowired
    private VideoDrive videoDrive;

    @Autowired
    private MockFile mockFile;

    @AfterEach
    public void cleanUp() {
        mockFile.cleanUp();
    }

    @Test
    @DisplayName("비디오 createFile 테스트")
    public void createFileTest() throws Exception {
        //given
        MultipartFile multipartFile = mockFile.createRandomFile("Test contents");

        //when
        String createdFileName = videoDrive.createFile(multipartFile);

        //then
        File createFile = mockFile.findFile(createdFileName);
        assertThat(createFile.exists()).isTrue();
    }

    @Test
    @DisplayName("비디오 getFile 테스트")
    public void getFileTest() throws Exception {
        //given
        String contents = UUID.randomUUID().toString() + "TestTestTestTestTestTest";
        MultipartFile multipartFile =  mockFile.createRandomFile(contents);
        String createdFileName = videoDrive.createFile(multipartFile);

        //when
        File getFile = videoDrive.getFile(createdFileName);

        //then
        String readContents = com.google.common.io.Files.asCharSource(getFile, StandardCharsets.UTF_8).read();
        assertThat(readContents).isEqualTo(contents);
    }

}
