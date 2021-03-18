package me.hoyoung.youtube.service.drive;

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
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiskDriveTest {

    @Autowired
    private DiskDrive videoDrive;

    @Autowired
    private MockFile mockFile;

    @AfterEach
    public void cleanUp() {
        mockFile.cleanUp();
    }

    @Test
    @DisplayName("createFile 테스트")
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
    @DisplayName("getFile 테스트")
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

    @Test
    @DisplayName("delete 테스트")
    public void deleteFileTest() throws Exception {
        //given
        String contents = "FILE DELETE 테스트용 파일 내용";
        MultipartFile multipartFile = mockFile.createRandomFile(contents);
        String createdFileName = multipartFile.getOriginalFilename();

        //when
        assertThat(mockFile.findFile(createdFileName)).isFile();
        videoDrive.delete(createdFileName);

        //then
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(() -> {
            videoDrive.delete(createdFileName);
        });
    }

}
