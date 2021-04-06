package me.hoyoung.youtube.service;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.comment.Comment;
import me.hoyoung.youtube.domain.comment.CommentRepository;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.video.Video;
import me.hoyoung.youtube.domain.video.VideoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public void addComment(String videoId, String content) {
        Video video = videoRepository.findById(videoId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.builder()
                .content(content)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .video(video)
                .author(user)
                .build();

        commentRepository.save(comment);
    }

}