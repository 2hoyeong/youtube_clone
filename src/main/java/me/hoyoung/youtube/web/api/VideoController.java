package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.service.VideoService;
import me.hoyoung.youtube.web.dto.VideoResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> createVideo(
            @RequestPart("content") MultipartFile file) throws IOException {
        Video metaData = videoService.createFile(file);
        Map<String, String> response = new HashMap();
        response.put("videoId", metaData.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}", produces = "video/mp4")
    public StreamingResponseBody getVideoStream(@PathVariable("id") String id) throws IOException {
        Video video = videoService.findById(id);
        File file = videoService.getFile(video.getOriginalFileName());
        final InputStream is = new FileInputStream(file);

        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) {
        try {
            byte[] data = new byte[4096];
            int read = 0;
            while ((read = is.read(data)) > 0) {
                os.write(data, 0, read);
                os.flush();
            }
        } catch(IOException ex) {

        } finally {
            try {
                is.close();
                os.close();
            } catch(IOException ex) {
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVideo(@PathVariable("id") String id) throws IOException {
        Video video = videoService.findById(id);
        videoService.delete(video);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponseDto> getVideoInfo(@PathVariable("id") String id) {
        Video video = videoService.findById(id);
        return new ResponseEntity<>(VideoResponseDto.builder()
                .id(video.getId())
                .uploader(video.getUploader().getName())
                .thumbnailPath(video.getThumbnailPath())
                .createdDate(video.getCreatedDate())
        .build(), HttpStatus.OK);
    }
}