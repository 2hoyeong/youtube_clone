package me.hoyoung.youtube.web.api;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.service.CommentService;
import me.hoyoung.youtube.web.dto.CommentAddDto;
import org.springframework.web.bind.annotation.*;

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

}