package me.hoyoung.youtube.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import me.hoyoung.youtube.web.dto.UserSignInDto;
import me.hoyoung.youtube.web.dto.UserSignUpDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

    private final String id = "ttttesssttt";
    private final String password = "testsestpassw";
    private final String name = "테스트트이름";

    @Test
    @DisplayName("회원가입 API 테스트")
    public void signUpTest() throws Exception {
        UserSignUpDto dto = new UserSignUpDto(User.builder().id(id).password(password).name(name).build());

        mvc.perform(post("/api/v1/user/signup")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        User user = userRepository.findById(id).orElse(null);
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("로그인 API 테스트")
    public void signInTest() throws Exception {
        UserSignInDto dto = new UserSignInDto(User.builder().id(id).password(password).build());

        userRepository.save(User.builder().id(id).password(password).name(name).build());

        mvc.perform(post("/api/v1/user/signin")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 비디오 리스트 API 테스트")
    public void getUserVideoListTest() throws Exception {
        User user = User.builder().id(id).password(password).name("username").build();
        String originFileName = "originFileName.txt";
        String thumbnailPath = "thumbnail.png";
        Timestamp createDate = new Timestamp(System.currentTimeMillis());

        userRepository.save(user);
        videoRepository.save(Video.builder()
                .uploader(user)
                .originalFileName(originFileName)
                .thumbnailPath(thumbnailPath)
                .createdDate(createDate)
        .build());

        ResultActions actions = mvc.perform(get("/api/v1/user/"+ user.getId() +"/videoList")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        Video video = videoRepository.findAll().get(0);

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(video.getId()))
                .andExpect(jsonPath("$[0].thumbnailPath").value(video.getThumbnailPath()));
    }

    @Test
    @DisplayName("토큰 검증 확인 API 제한 테스트")
    public void tokenValidatorThrowsTooManyRequests() throws Exception {
        for(int i = 0; i <= 5; i++) {
            mvc.perform(get("/api/v1/user"))
                    .andExpect(status().isOk());
        }
        mvc.perform(get("/api/v1/user"))
                .andExpect(status().isTooManyRequests());

    }

}