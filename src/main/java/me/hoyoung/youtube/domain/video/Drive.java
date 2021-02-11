package me.hoyoung.youtube.domain.video;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface Drive {
    String createFile(MultipartFile file) throws IOException;
    File getFile(String originName) throws IOException;
    void delete(String filename) throws IOException;
}
