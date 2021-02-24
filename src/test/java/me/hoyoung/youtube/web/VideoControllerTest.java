package me.hoyoung.youtube.web;

import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import me.hoyoung.youtube.util.MockFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void videoSaveApiTest() throws Exception {
        MultipartFile mockVideo = mockFile.createRandomFile("videoSaveAPI TEST");

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/video/upload")
                .file("content", mockVideo.getBytes())
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
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
}
