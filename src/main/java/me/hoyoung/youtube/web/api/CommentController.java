package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.comment.CommentListDao;
import me.hoyoung.youtube.service.CommentService;
import me.hoyoung.youtube.web.dto.CommentAddDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;


    @PostMapping(value = "/{videoId}")
    public void createVideo(
            @RequestBody CommentAddDto commentAddDto, @PathVariable("videoId") String videoId) {
        commentService.addComment(videoId, commentAddDto.getContent());
    }

    @GetMapping(value = "/{videoId}")
    public ResponseEntity<List<CommentListDao>> getVideoComments(@PathVariable("videoId") String videoId) {
        List<CommentListDao> commentList = commentService.getCommentList(videoId);
        return new ResponseEntity(commentList, HttpStatus.OK);
    }

}
