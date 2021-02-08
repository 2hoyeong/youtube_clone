package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.service.VideoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

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
}
