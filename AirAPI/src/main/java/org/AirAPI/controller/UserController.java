package org.AirAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.Messege;
import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final HeaderSetter headerSetter;
    //@Autowired
    //private final CustomUserDetailService customUserDetailService;
/*
    final String BIRTH = "001200";
    final String EMAIL = "aabbcc@gmail.com";
    final String NICKNAME = "침착맨";
    final Long SEQUENCEID = Long.valueOf(1);

    User user = User.builder()
            .userEmail(EMAIL)
            .userBirth(BIRTH)
            .userNickname(NICKNAME)
            .userSequenceId(SEQUENCEID)
            .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
            .build();

    // 회원가입
    @PostMapping("/join")
    public String join(@RequestBody User user){
        customUserDetailService.save(user);
        return user.toString();
    }
    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {

        //User member = (User) customUserDetailService.loadUserByEmail(user.getUserEmail());
        // 토큰 생성
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        // 리프레시 토큰 생성
        String refreshtoken = jwtTokenProvider.refreshToken(member.getUsername());
        RefreshToken re_token = RefreshToken.builder()
                .username(member.getUsername())
                .token(refreshtoken)
                .build();
        customUserDetailService.toeknsave(re_token);
        ResponseEntity header = headerSetter.haederSet(token, "login Success", HttpStatus.OK);
        return header;
    }
*/

}