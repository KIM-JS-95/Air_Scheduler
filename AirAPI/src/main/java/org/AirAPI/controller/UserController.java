package org.AirAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final HeaderSetter headerSetter;
    @Autowired
    private final CustomUserDetailService customUserDetailService;

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


    @PostMapping("/join")
    public String join(){
        customUserDetailService.save(user);
        return user.toString();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> user) {
        /*
            로그인 정보 획득 -> 메인 토큰 생성 / 리프레시 토큰 호출
            -> 리프레시 토큰 (만료 / 없음) 생성
        */
        User member = (User) customUserDetailService.loadUserByEmail(user.get("email"));
        // 토큰 생성
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        // 리프레시 토큰 생성
        String refreshtoken = jwtTokenProvider.refreshToken(member.getUsername());
        RefreshToken re_token = RefreshToken.builder()
                .username(member.getUsername())
                .token(refreshtoken)
                .build();
        customUserDetailService.toeknsave(re_token);
        ResponseEntity header = headerSetter.haederSet(token);
        return header;
    }

    @PostMapping("/2")
    public String logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return token;
    }
}