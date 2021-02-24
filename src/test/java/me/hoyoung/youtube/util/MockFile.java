package me.hoyoung.youtube.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Component
public class MockFile {

    @Value("${video.storage.dir}")
    private String directory;

    private String testExtension = ".tests";

    @PostConstruct
    public void createTestDirectory() {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("테스트용 디렉토리 생성 완료");
        }
    }


    public MultipartFile createRandomFile(String contents) throws Exception {
        String filename = UUID.randomUUID().toString() + testExtension;
        byte[] contentBytes = contents.getBytes();
        Path path = Paths.get(directory + filename);
        Files.write(path, contentBytes);
        return new MockMultipartFile(filename, filename, "text/plain",  new FileInputStream(new File(directory + filename)));
    }

    public void cleanUp() {
        final File[] files = new File(directory).listFiles((dir, name) -> name.matches( ".+("+ testExtension +"|null)$" ));
        Arrays.stream(files).forEach((file) -> file.delete());
    }

    public File findFile(String filename) {
        return new File(directory + filename);
    }

}
