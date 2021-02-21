package me.hoyoung.youtube.web;

import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    @Value("${video.storage.dir}")
    private String directory;

    private String testExtension = ".tests";
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
        cleanUp();
    }

    public void cleanUp() {
        final File[] files = new File(directory).listFiles((dir, name) -> name.matches( ".+("+ testExtension +")$" ));
        Arrays.stream(files).forEach((file) -> file.delete());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void videoSaveApiTest() throws Exception {
        MultipartFile mockVideo = createTestRandomFile("videoSaveAPI TEST");

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/video/upload")
                .file("content", mockVideo.getBytes())
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    public MultipartFile createTestRandomFile(String contents) throws Exception {
        String filename = UUID.randomUUID().toString() + testExtension;
        byte[] contentBytes = contents.getBytes();
        Path path = Paths.get(directory + filename);
        Files.write(path, contentBytes);
        return new MockMultipartFile(filename, filename, "text/plain",  new FileInputStream(new File(directory + filename)));
    }
}
