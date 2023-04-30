package org.AirAPI.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final HeaderSetter headerSetter;

    @Autowired
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;


    // 회원가입
    @PostMapping("/join")
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
        String refreshtoken = jwtTokenProvider.createrefreshToken(member.getUsername());
        customUserDetailService.token_save(refreshtoken);

        ResponseEntity header = headerSetter.haederSet(token, "login Success", HttpStatus.OK);
        return header;
    }


}