package me.hoyoung.youtube.config.auth;

import lombok.RequiredArgsConstructor;
import me.hoyoung.youtube.domain.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private String[] NO_ROLE_PAGE = {
            "/api/v1/user/signin",
            "/api/v1/user/signup",
            "/h2-console/**",
            "/api/v1/video/view/*",
            "/api/v1/video/*",
            "/api/v1/user/profileImage/**",
            "/api/v1/user/*/profileImage",
            "/api/v1/video/*/thumbnail",
            "/api/v1/comment/list/*",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .antMatchers(NO_ROLE_PAGE).permitAll()
                        .anyRequest().hasRole(Role.USER.name())
                .and()
                    .cors()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                            UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}