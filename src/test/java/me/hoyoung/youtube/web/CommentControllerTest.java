package me.hoyoung.youtube.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoyoung.youtube.domain.comment.Comment;
import me.hoyoung.youtube.domain.comment.CommentRepository;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import me.hoyoung.youtube.web.dto.CommentAddDto;
import me.hoyoung.youtube.web.dto.CommentPatchDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private UserDetails userDetails;
    private Video mockVideo;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        createMockUser();
        createMockVideo();
    }

    public void createMockUser() {
        String id = "testId";
        String password = "testPassword";
        String name = "테스트용 계정";
        userRepository.save(User.builder().id(id).name(name).password(password).build());
        userDetails = userRepository.findAll().get(0);
    }

    public void createMockVideo() {
        String originFileName = "oiiowjoiwjakl.mp4";
        String thumbnailPath = "zxcvjioipasd.png";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        mockVideo = Video.builder()
                .uploader((User) userDetails)
                .originalFileName(originFileName)
                .thumbnailPath(thumbnailPath)
                .createdDate(timestamp).build();
        videoRepository.save(mockVideo);
    }

    @AfterEach
    public void tearDown() {
        commentRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성 API 테스트")
    public void createVideoCommentTest() throws Exception {
        String contents = "댓글 작성 API 테스트 댓글 내용";
        String videoId = mockVideo.getId();

        CommentAddDto dto = new CommentAddDto(contents);

        mvc.perform(post("/api/v1/comment/" + videoId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(dto))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(contents);
    }


    @Test
    @DisplayName("댓글 조회 API 테스트")
    public void getVideoCommentsTest() throws Exception {
        String contents = "댓글 조회 API 테스트 댓글 내용" + UUID.randomUUID();
        String videoId = mockVideo.getId();
        commentRepository.save(Comment.builder()
                .author((User) userDetails)
                .video(mockVideo)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .content(contents)
                .build());

        ResultActions actions = mvc.perform(get("/api/v1/comment/list/" + videoId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(contents));
    }

    @Test
    @DisplayName("댓글 삭제 API 테스트")
    public void deleteCommentTest() throws Exception {
        String contents = "댓글 삭제 API 테스트";
        commentRepository.save(Comment.builder()
                .author((User) userDetails)
                .video(mockVideo)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .content(contents)
                .build());

        Comment comment = commentRepository.findAll().get(0);

        assertThat(comment.getContent()).isEqualTo(contents);

        mvc.perform(delete("/api/v1/comment/" + comment.getCuid())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(user(userDetails)));

        List<Comment> commentList = commentRepository.findAll();

        assertThat(commentList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    public void patchCommentTest() throws Exception {
        String contents = "댓글 수정 API 테스트";
        String patchedContents = "댓글 수정 후 내용 " + UUID.randomUUID();

        commentRepository.save(Comment.builder()
                .author((User) userDetails)
                .video(mockVideo)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .content(contents)
                .build());
        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(contents);

        CommentPatchDto dto = new CommentPatchDto(patchedContents);

        mvc.perform(patch("/api/v1/comment/" + comment.getCuid())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(dto))
                .with(user(userDetails)))
                .andExpect(status().isOk());

        Comment patchedComment = commentRepository.findAll().get(0);
        assertThat(patchedComment.getContent()).isEqualTo(patchedContents);
    }
}
