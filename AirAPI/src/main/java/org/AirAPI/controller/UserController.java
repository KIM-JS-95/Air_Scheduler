package org.AirAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final HeaderSetter headerSetter;
    private final UserRepository userRepository;

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
        log.info("로그인 시도됨");
        userRepository.save(user);

        return user.toString();

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> user) {
        User member = (User) userRepository.findByUserEmail(user.get("email"));
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        ResponseEntity header = headerSetter.HaederSet(token);

        return header;
    }
}