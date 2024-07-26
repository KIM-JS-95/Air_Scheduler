package org.air.jwt;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.air.entity.User;
import org.air.service.CustomUserDetailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // 토큰값 획득
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token)) { // 유효할 경우 처리

            // 유효하지만 토큰 시간이 얼마 남지 않았을경우(3분 이하) => true
            if(jwtTokenProvider.isTokenExpiringSoon(token)){
                Date date = new Date();
                SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");

                String userid = jwtTokenProvider.getUserPk(token);
                User user = customUserDetailService.loadUserByToken(userid);
                String new_token = jwtTokenProvider.createrefreshToken(user.getUserid(), access_time.format(date), user.getAuthority().getAuthority());

                customUserDetailService.token_save(user, new_token);
            }
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}