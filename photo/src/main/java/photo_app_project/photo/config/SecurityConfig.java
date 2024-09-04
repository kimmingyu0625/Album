package photo_app_project.photo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import photo_app_project.photo.security.custom.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 폼 기반 로그인 비활성화
        http.formLogin((login) -> login.disable());
        // HTTP 기본 인증 비화설화
        http.httpBasic((basic) -> basic.disable());
        // CSRF 공격 방어 기능 비활성화
        http.csrf((csrf) -> csrf.disable());
        // 세션 관리 정책 설정
        // 세션 인증을 사용하지않고 JWT를 사용
        http.sessionManagement((management) -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterAt(null, null)
                .addFilterBefore(null, null);

        http.authorizeHttpRequests(authorizaRequests ->
                authorizaRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .anyRequest().authenticated()
        );

        //인증방식
        http.userDetailsService(customUserDetailService);


        return http.build();
    }
}
