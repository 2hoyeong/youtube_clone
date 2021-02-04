package me.hoyoung.youtube.domain.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VideoDrive {

    @Value("${video.storage.dir}")
    private String directory;

    public String createFile(MultipartFile file) throws IOException{
        String fileName = String.format(
                "%s.%s",
                UUID.randomUUID().toString(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );

        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String location = new StringBuilder(directory).append(File.separator).append(fileName).toString();
        Path path = Paths.get(location);
        Files.write(path, file.getBytes());
        return location;
    }

    public byte[] getFile(String path) throws IOException {
        File file = new File(path);
        byte[] b = null;
        if (file.exists()) {
            b = Files.readAllBytes(file.toPath());
        }
        return b;
    }
}
