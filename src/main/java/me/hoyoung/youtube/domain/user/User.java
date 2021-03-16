package me.hoyoung.youtube.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hoyoung.youtube.domain.video.Video;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    private final String DEFAULT_PROFILE_IMAGE_NAME = "default.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;

    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String password;

    @Column
    private String role = Role.USER.getKey();

    @OneToMany(mappedBy = "uploader")
    private List<Video> videos = new ArrayList<>();

    @Column
    private String profileImage = DEFAULT_PROFILE_IMAGE_NAME;

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(this.role));
        return auth;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
