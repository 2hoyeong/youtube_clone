package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.service.VideoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping(
            value = "/upload",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void createVideo(
            @RequestPart("content") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        Video metaData = videoService.createFile(file);
    }
}
