package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoDrive;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoDrive videoDrive;

    public Video createFile(MultipartFile file) throws IOException {
        String filename = videoDrive.createFile(file);

        Video video = Video.builder()
                .originalFileName(file.getName())
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .originalFileName(filename)
                .build();


        return videoRepository.save(video);
    }

    public Video findById(String id) {
        return videoRepository.findById(id);
    }

    public byte[] getFile(String originName) throws IOException {
        return videoDrive.getFile(originName);
    }

}
