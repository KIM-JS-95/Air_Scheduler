package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.*;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    // 로그인
    @PostMapping("/login")
    public ResponseEntity login( @RequestBody User user) {
        User member = customUserDetailService.loadUserByUser(user);

        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        }

        Date date = new Date();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));

        customUserDetailService.token_save(member, token);

        return ResponseEntity.ok()
                .headers(headerSetter.haederSet(token, "login Success"))
                .body("Login Success");
    }

    @PostMapping("/user_modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody User user) {

        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        boolean flag = customUserDetailService.modify(user, user_string);
        if (flag) {
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String new_token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(date));

            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(new_token, "login Success"))
                    .body("Login Success");
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                    .body("");
        }
    }


    @PostMapping("/getuserinfobyToken")
    public ResponseEntity getUserinfo(@RequestHeader("Authorization") String token) {
        String user_token = jwtTokenProvider.getUserPk(token); // body: userid
        User user = customUserDetailService.loadUserByToken(user_token);

        // Create a JSON object with required fields
        JSONObject member = new JSONObject();
        member.put("userid", user.getUserid());
        member.put("email", user.getEmail());
        member.put("password", user.getPassword());

        return ResponseEntity.ok()
                .headers(headerSetter.haederSet(token, "login Success"))
                .body(member.toString());
    }

    // 로그아웃 (서블렛 토큰 제거)
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        String user_string = jwtTokenProvider.getUserPk(token);

        if (customUserDetailService.logout(user_string)) {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "logout fail"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(null, "logout Success"))
                    .body("");
        }
    }


    // ----------------------------------- 어드민 권한 -----------------------------------
    // 회원가입
    @PostMapping("/join/save/user")
    public ResponseEntity join(@RequestBody UserDTO user) {
        HttpStatus status = HttpStatus.CREATED; // 응답 상태 기본값 설정

        // 사용자 타입에 따라 서비스 메서드 호출
        if (user.getType().equals("USER")) { // 승무원일 경우
            int result = customUserDetailService.savePilot(user);
            if (result == 3) {
                status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()));
            }
        } else if (user.getType().equals("FAMILY")) { // 가족일 경우
            int result = customUserDetailService.saveFamily(user);
            if (result == 3) {
                status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()));
            }
        }

        return ResponseEntity.status(status).body(user);
    }

    // 회원가입 이전 임시 저장 -> 해당 유저와 컨텍트 후(랜덤키 전달) 회원가입 처리
    @PostMapping("/join/save/pilotcode")
    public ResponseEntity save_pilotcode(@RequestBody TemppilotcodeDAO temppilotcode) {
        Temppilotcode result = customUserDetailService.save_pilotcode(temppilotcode);

        if (result != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("");
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .body("");
        }
    }

}