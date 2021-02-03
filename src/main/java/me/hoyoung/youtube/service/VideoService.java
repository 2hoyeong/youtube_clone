package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public Video createFile(MultipartFile file) {
        Video video = Video.builder()
                .originalFileName(file.getName().toString())
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return videoRepository.save(video);
    }

}
