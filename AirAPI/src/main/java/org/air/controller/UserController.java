package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 사용자 관리 기능 모음집 <br>
 * Function <br>
     * join: 회원가입 <br>
     * login: 유저 로그인 <br>
     * log_out: 유저 로그 아웃 <br>
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    private HeaderSetter headerSetter;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody User user) {
        customUserDetailService.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request, @RequestBody User user) {

        Date date = new Date();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        User member = customUserDetailService.loadUserById(user.getUserid());

        if (member == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));
        customUserDetailService.token_save(member, token);
        log.info("token: "+token);
        HttpHeaders header = headerSetter.haederSet(token, "login Success");

        return ResponseEntity.ok()
                .headers(header)
                .body("Login Success");
    }

    // 로그아웃 (서블렛 토큰 제거)
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        boolean result = customUserDetailService.logout(request);
        HttpHeaders header = headerSetter.haederSet("", "logout Success");
        return ResponseEntity.ok()
                .headers(header)
                .body("Logout Success");
    }
}