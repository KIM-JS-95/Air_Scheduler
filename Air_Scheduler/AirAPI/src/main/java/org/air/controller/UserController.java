package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.Messege;
import org.air.entity.StatusEnum;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
        // 회원가입 성공시 로그인 페이지로 이동시켜주기
        if (customUserDetailService.save(user)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(user);
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .body("");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request, @RequestBody User user) {

        Date date = new Date();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        User member = customUserDetailService.loadUserByUser(user);

        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        }

        String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));

        customUserDetailService.token_save(member, token);

        return ResponseEntity.ok()
                .headers(headerSetter.haederSet(token, "login Success"))
                .body("Login Success");
    }

    @PostMapping("/user_modify")
    public ResponseEntity modify(HttpServletRequest request, @RequestBody User user){
        String token = request.getHeader("Authorization");
        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        boolean flag = customUserDetailService.modify(user, user_string);
        if(flag) {
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String new_token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(date));

            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(new_token, "login Success"))
                    .body("Login Success");
        }else{
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                    .body(new Messege(StatusEnum.SAVE_ERROR.getStatusCode(),
                            StatusEnum.SAVE_ERROR.getMessage()));
        }
    }


    @PostMapping("/getuserinfobyToken")
    public ResponseEntity getUserinfo(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String user_token = jwtTokenProvider.getUserPk(token); // body: userid
        User user = customUserDetailService.loadUserByToken(user_token);

        // Create a JSON object with required fields
        JSONObject member = new JSONObject();
        member.put("userid", user.getUserid());
        member.put("email", user.getEmail());
        member.put("password", user.getPassword());
        System.out.println(member.toString());
        return ResponseEntity.ok()
                .headers(headerSetter.haederSet(token, "login Success"))
                .body(member.toString());
    }

    // 로그아웃 (서블렛 토큰 제거)
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        User user = customUserDetailService.loadUserByToken(user_string);

        if (customUserDetailService.logout(user.getUserid())) {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "logout fail"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(null, "logout Success"))
                    .body("");
        }
    }
}