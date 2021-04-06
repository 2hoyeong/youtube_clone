package me.hoyoung.youtube.domain.comment;

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
}
