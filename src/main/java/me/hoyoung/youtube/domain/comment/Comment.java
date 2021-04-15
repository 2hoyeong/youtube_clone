package me.hoyoung.youtube.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import me.hoyoung.youtube.domain.video.Video;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cuid;

    @Column(columnDefinition ="Text")
    private String content;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn
    private User author;

    @ManyToOne
    @JoinColumn
    private Video video;

    public void setContent(String content) {
        this.content = content;
    }

    @Builder
    public Comment(String content, Timestamp createdDate, User author, Video video) {
        this.content = content;
        this.createdDate = createdDate;
        this.author = author;
        this.video = video;
    }
}
