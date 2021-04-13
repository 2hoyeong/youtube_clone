package me.hoyoung.youtube.domain.comment;

import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.user.UserRepository;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    private User mockUser;
    private Video mockVideo;

    @BeforeEach
    public void setupTest() {
        createMockUser();
        createMockVideo();
    }

    @AfterEach
    public void cleanUp() {
        commentRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void createMockUser() {
        String id = "testId";
        String password = "testPassword";
        String name = "테스트용 계정";
        mockUser = User.builder().id(id).name(name).password(password).build();
        userRepository.save(mockUser);
    }

    public void createMockVideo() {
        String originFileName = "oiiowjoiwjakl.mp4";
        String thumbnailPath = "zxcvjioipasd.png";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        mockVideo = Video.builder()
                .uploader(mockUser)
                .originalFileName(originFileName)
                .thumbnailPath(thumbnailPath)
                .createdDate(timestamp).build();
        videoRepository.save(mockVideo);
    }

    @Test
    @Transactional
    @DisplayName("Comment 저장 테스트")
    public void saveCommentTest() {
        //given
        String commentContent = "댓글 테스트 내용";
        commentRepository.save(Comment.builder()
                .author(mockUser)
                .video(mockVideo)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .content(commentContent)
                .build());

        //when
        Comment comment = commentRepository.findAll().get(0);

        //then
        assertThat(comment.getAuthor()).isEqualTo(mockUser);
        assertThat(comment.getVideo()).isEqualTo(mockVideo);
        assertThat(comment.getContent()).isEqualTo(commentContent);
    }

    @Test
    @Transactional
    @DisplayName("비디오의 Comment 조회 테스트")
    public void findCommentByVideoIdTest() {
        //given
        String[] commentsContent = {
                "댓글 테스트 내용1",
                "댓글 테스트 내용2"
        };
        Arrays.stream(commentsContent).forEach(comment -> {
            commentRepository.save(Comment.builder()
                    .author(mockUser)
                    .video(mockVideo)
                    .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                    .content(comment)
                    .build());
        });
        Video tempMockVideo = Video.builder()
                .uploader(mockUser)
                .originalFileName("qwioqoqewqeiwoqw.mp4")
                .createdDate(Timestamp.valueOf(LocalDateTime.now())).build();
        videoRepository.save(tempMockVideo);
        commentRepository.save(Comment.builder()
                .author(mockUser)
                .video(tempMockVideo)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .content("조회되면 안되는 댓글")
                .build());

        //when
        List<CommentListDao> commentList = commentRepository.findAllComment(mockVideo);

        //then
        assertThat(commentsContent.length).isEqualTo(commentList.size());
        for (int i = 0; i < commentList.size(); i++) {
            assertThat(commentList.get(i).getContent()).isEqualTo(commentsContent[i]);
            assertThat(commentList.get(i).getName()).isEqualTo(mockUser.getName());
        }
    }

    @Test
    @Transactional
    @DisplayName("Comment 삭제 테스트")
    public void deleteCommentTest() {
        //given
        String[] commentsContent = {
                "댓글 테스트 내용1",
                "댓글 테스트 내용2",
                "댓글 테스트 내용3",
                "댓글 테스트 내용4",
                "댓글 테스트 내용5",
                "댓글 테스트 내용6",

        };
        Arrays.stream(commentsContent).forEach(comment -> {
            commentRepository.save(Comment.builder()
                    .author(mockUser)
                    .video(mockVideo)
                    .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                    .content(comment)
                    .build());
        });
        List<CommentListDao> commentList = commentRepository.findAllComment(mockVideo);

        //when
        commentList.stream().forEach(comment -> {
            commentRepository.deleteByAuthorAndCuid(mockUser, comment.getCuid());
        });

        //then
        assertThat(commentRepository.findAllComment(mockVideo).size()).isEqualTo(0);
    }
}
