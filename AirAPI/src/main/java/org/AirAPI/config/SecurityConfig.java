package org.AirAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {
    //@Autowired
    //private JwtAuthenticationFilter jwtAuthenticationFilter;
        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/api/v1/login"
            );
        }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        return http.httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/home","/upload","/join", "/login","/h2-console/**").permitAll()
                .antMatchers("/").authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}