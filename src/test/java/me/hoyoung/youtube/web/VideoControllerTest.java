package me.hoyoung.youtube.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import me.hoyoung.youtube.util.MockFile;
import me.hoyoung.youtube.web.dto.VideoTitleModifyDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VideoControllerTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockFile mockFile;

    private MockMvc mvc;
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.save(User.builder().id("test").name("test").password("testpassword").build());
        userDetails = userRepository.findAll().get(0);
    }

    @AfterEach
    public void tearDown() {
        videoRepository.deleteAll();
        userRepository.deleteAll();
        mockFile.cleanUp();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("비디오 저장 API 테스트")
    public void videoSaveApiTest() throws Exception {
        MultipartFile mockVideo = mockFile.createRandomFile("videoSaveAPI TEST");

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/video/upload")
                .file("content", mockVideo.getBytes())
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("비디오 스트림 API 테스트")
    public void videoStreamTest() throws Exception {
        String innerText = "videoStreamAPI TEST";
        MultipartFile mockVideo = mockFile.createRandomFile(innerText);
        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/video/upload")
                .file("content", mockVideo.getBytes())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Video video = videoRepository.findAll().get(0);
        String videoId = video.getId();

        mvc.perform(get("/api/v1/video/view/" + videoId))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andExpect(content().string(innerText));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("비디오 정보 조회 테스트")
    public void getVideoInfoTest() throws Exception {
        String innerText = "getVideoInfoTest API TEST";
        MultipartFile mockVideo = mockFile.createRandomFile(innerText);
        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/video/upload")
                .file("content", mockVideo.getBytes())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Video video = videoRepository.findAll().get(0);
        String videoId = video.getId();

        ResultActions actions = mvc.perform(get("/api/v1/video/" + videoId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(video.getId()))
                .andExpect(jsonPath("thumbnailPath").value(video.getThumbnailPath()))
                .andExpect(jsonPath("name").value(video.getUploader().getName()))
                .andExpect(jsonPath("profileImage").value(video.getUploader().getProfileImage()))
                .andExpect(jsonPath("views").value(video.getViews()))
                .andExpect(jsonPath("title").value(video.getTitle()));
    }

    @Test
    @DisplayName("비디오 타이틀 수정 테스트")
    public void setVideoTitleTest() throws Exception {
        String videoTitle = "비디오 타이틀";
        videoRepository.save(Video.builder()
        .createdDate(new Timestamp(System.currentTimeMillis()))
        .originalFileName("originFileName")
        .uploader((User) userDetails)
        .build());

        Video video = videoRepository.findAll().get(0);
        String videoId = video.getId();
        assertThat(video.getTitle()).isEqualTo(null);

        VideoTitleModifyDto videoTitleVo = new VideoTitleModifyDto(videoId, videoTitle);

        mvc.perform(patch("/api/v1/video/title")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(videoTitleVo))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        video = videoRepository.findAll().get(0);

        assertThat(video.getTitle()).isEqualTo(videoTitle);
    }
}
