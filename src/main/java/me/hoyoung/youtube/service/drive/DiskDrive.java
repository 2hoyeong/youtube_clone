package me.hoyoung.youtube.service.drive;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class DiskDrive implements Drive {

    private String directory;

    @Override
    public String createFile(MultipartFile file) throws IOException {
        String fileName = String.format(
                "%s.%s",
                UUID.randomUUID().toString(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );

        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Path path = Paths.get(directory + fileName);
        Files.write(path, file.getBytes());
        return fileName;
    }

    @Override
    public File getFile(String originName) throws IOException {
        String location = directory + originName;
        return new File(location);
    }

    @Override
    public void delete(String filename) throws IOException {
        File file = new File(directory + filename);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("비디오를 삭제할 수 없습니다.");
            }
        } else {
            throw new FileNotFoundException("비디오를 찾을 수 없습니다.");
        }
    }
}
