package me.hoyoung.youtube.domain.video;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoyoung.youtube.domain.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Entity
public class Video {

    @Id
    @GenericGenerator(name = "uuid_gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid_gen")
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "uuid")
    private User uploader;

    @Column(nullable = false)
    private String originalFileName;

    @Column
    private String thumbnailPath;

    @Column(nullable = false)
    private Timestamp createdDate;

}
