package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoDrive;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoDrive videoDrive;

    @Transactional
    public Video createFile(MultipartFile file) throws IOException {
        String filename = videoDrive.createFile(file);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Video video = Video.builder()
                .uploader(user)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .originalFileName(filename)
                .build();


        return videoRepository.save(video);
    }

    public Video findById(String id) {
        return videoRepository.findById(id);
    }

    public File getFile(String originName) throws IOException {
        return videoDrive.getFile(originName);
    }

    @Transactional
    public void delete(Video video) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (video.getUploader() == user) {
            videoDrive.delete(video.getOriginalFileName());
            videoRepository.delete(video);
        } else {
            throw new AuthenticationException("삭제할 수 없습니다.");
        }
    }
}
