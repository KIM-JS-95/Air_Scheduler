package org.AirAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private HeaderSetter headerSetter;

    @Autowired
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;


    // 회원가입
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public String join(@RequestBody User user){
        customUserDetailService.save(user);
        return user.toString();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {

        Date  date= new Date();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        User member = customUserDetailService.loadUserById(user.getUserId());

        String token = jwtTokenProvider.createToken(member.getUserId(), access_time.format(date));
        String refreshtoken = jwtTokenProvider.createrefreshToken(member.getName());
        customUserDetailService.token_save(refreshtoken);

        HttpHeaders header = headerSetter.haederSet(token, "login Success");

        return ResponseEntity.ok()
                .headers(header)
                .body("Login Success");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity logout(){
        String token="";
        HttpHeaders header = headerSetter.haederSet(token, "logout Success");
        return ResponseEntity.ok()
                .headers(header)
                .body("Logout Success");
    }
}