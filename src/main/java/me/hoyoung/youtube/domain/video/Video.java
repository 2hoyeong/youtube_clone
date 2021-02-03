package me.hoyoung.youtube.domain.video;

import lombok.Builder;
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
    @JoinColumn
    private User uploader;

    @Column(name="original_FileName", nullable = false)
    private String originalFileName;

    @Column
    private String thumbnailPath;

    @Column(name="created_date", nullable = false)
    private Timestamp createdDate;

    @Builder
    public Video(User uploader, String originalFileName, String thumbnailPath, Timestamp createdDate) {
        this.uploader = uploader;
        this.originalFileName = originalFileName;
        this.thumbnailPath = thumbnailPath;
        this.createdDate = createdDate;
    }

}
