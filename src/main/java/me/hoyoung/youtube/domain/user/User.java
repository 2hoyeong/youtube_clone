package me.hoyoung.youtube.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uuid;
    private String id;
    private String name;
    private String password;
}
