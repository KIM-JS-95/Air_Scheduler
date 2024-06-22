package org.air.config;

import org.air.jwt.JwtAuthenticationFilter;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/api/v1/login", "/h2-console/**"
        );
    }

    @Bean
    public HttpFirewall allowUrlEncodedDoubleSlashFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/join", "/login","/join/save/pilotcode","/device").permitAll()
                .antMatchers("/admin/fcm").hasAuthority("ADMIN")
                .antMatchers("/getschedule","/showschedules", "/getnationcode", "/gettodayschedule","/viewschedule").authenticated()
                .antMatchers("/home", "/modify", "/delete", "/upload").authenticated()

                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Value("${secretKey}")
    private String secretKey;

    @Value("${salt}")
    private String salt;


    @Bean
    public TextEncryptor textEncryptor() {
        // AES 알고리즘을 사용한 TextEncryptor 생성
        return Encryptors.text(secretKey, salt);
    }


    // CORS 설정 추가
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}