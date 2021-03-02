package me.hoyoung.youtube.service;

import io.jsonwebtoken.lang.Assert;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class ThumbnailService {

    @Value("${ffmpeg.dir}")
    private String ffmpegDir;

    @Value("${ffprobe.dir}")
    private String ffprobeDir;

    @Value("${thumbnail.storage.dir}")
    private String thumbnailPath;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    public void init() throws IOException {
        ffmpeg = new FFmpeg(ffmpegDir);
        ffprobe = new FFprobe(ffprobeDir);

        Assert.isTrue(ffmpeg.isFFmpeg());
        Assert.isTrue(ffprobe.isFFprobe());

    }

    public void createThumbnail(String filePath, String thumbnailPath){
        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 오버라이드 여부
                .setInput(filePath) // 썸네일 생성대상 파일
                .addExtraArgs("-ss", "00:00:01") // 썸네일 추출 시작점
                .addOutput(thumbnailPath) // 썸네일 파일의 Path
                .setFrames(1) // 프레임 수
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}

