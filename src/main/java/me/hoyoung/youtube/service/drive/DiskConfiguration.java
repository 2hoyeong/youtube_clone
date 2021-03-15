package me.hoyoung.youtube.service.drive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiskConfiguration {

    @Bean
    @Value("${video.storage.dir}")
    public DiskDrive videoDrive(String videoDriectory) {
        return new DiskDrive(videoDriectory);
    }

    @Bean
    @Value("${profile.storage.dir}")
    public DiskDrive profileDrive(String profileDirectory) {
        return new DiskDrive(profileDirectory);
    }
}
