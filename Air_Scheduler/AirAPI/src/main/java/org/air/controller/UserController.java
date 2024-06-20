package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.*;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.EmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
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

    @Autowired
    private EmailService emailService;


    @GetMapping("/servertest")
    public ResponseEntity server_test(){
        return ResponseEntity.ok()
                .headers(headerSetter.haederSet("", "login Success"))
                .body("Login Success");
    }

    // 로그인 이메일 주소 빼고 jejuair.nat으로 만
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO user) throws MessagingException, IOException {
        User member = customUserDetailService.loadUserByUser(user);

        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        } else { // 안드로이드 ID값이 다르면? -> 임시 계정 테이블에 저장 -> 이메일로 전달
            if (!user.getAndroidid().equals(member.getAndroidid())) {
                TemppilotcodeDAO temppilotcodeDAO = TemppilotcodeDAO.builder()
                        .email(member.getEmail())
                        .username(member.getName())
                        .userid(member.getUserid())
                        .build();

                emailService.sendLoginCautionMail(member.getName(), member.getEmail(), member.getUserid(), user.getAndroidid());
                customUserDetailService.login_check(temppilotcodeDAO); // 임시저장
                return ResponseEntity.status(Integer.parseInt(StatusEnum.DEVICE_NOT_MATCH.getStatusCode()))
                        .headers(headerSetter.haederSet("", StatusEnum.DEVICE_NOT_MATCH.getMessage()))
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
    }

    @PostMapping("/user_modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody User user) {

        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        boolean flag = customUserDetailService.modify(user, user_string);
        if (flag) {
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String new_token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(date));

            User member = customUserDetailService.loadUserByToken(user.getUserid());
            customUserDetailService.token_save(member, token);

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


    // ----------------------------------- 회원가입 권한 -----------------------------------

    // 회원가입 이전 임시 저장 -> 해당 유저에게 이메일을 전달후(랜덤키 전달) 회원가입 처리 : <userid>@jejuair.net
    @PostMapping("/join/save/pilotcode")
    public ResponseEntity join(@RequestBody TemppilotcodeDAO temppilotcode) {
        Temppilotcode result = customUserDetailService.save_pilotcode(temppilotcode);
        if (result != null) {
            boolean emailSent = emailService.sendTokenMail(result.getUsername(), result.getEmail(), result.getRandomkey());
            if (emailSent) { // 메일 & 임시 회원 성공
                return ResponseEntity.status(HttpStatus.CREATED).body("");
            } else { // 메일 전송에 실패
                customUserDetailService.delete_temp_pilotcode(temppilotcode);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    // 최종 회원가입 (디바이스 토큰도 같이 획득)
    @PostMapping("/join/save/user/fin")
    public ResponseEntity join_family_fin(@RequestBody UserDTO user) { // password, randomkey, androidid
        HttpStatus status = HttpStatus.CREATED;

        int result = customUserDetailService.savePilot(user);
        if (result == 0) { // 존재하지 않는
            status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()));
        }

        return ResponseEntity.status(status).body("");
    }

    @PostMapping("/join/save/family")
    public ResponseEntity join_family(@RequestBody UserDTO user) throws MessagingException { // family_id ,userid, password, androidid
        User user1 = customUserDetailService.getUser(user.getFamily_id());
        TemppilotcodeDAO temppilotcodeDAO = TemppilotcodeDAO.builder()
                .userid(user.getUserid()) // 가족 아이디
                .password(user.getPassword()) // 가족 비번
                .username(user.getName()) // 가족 이름
                .pilotid(user1.getUserid())
                .build();
        customUserDetailService.processPilotcode(temppilotcodeDAO); // 임시저장

        String mgs = user.getName()+"님과 일정을 공유합니다.";
        emailService.sendMail(user.getUserid(), user1.getName(), user.getName(), user1.getEmail(), mgs, user.getAndroidid());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }


    @GetMapping("/join/save/family/fin")
    public ResponseEntity join_family_fin(@RequestParam("userid") String userid, @RequestParam("androidid") String androidid){
        User family = customUserDetailService.saveFamily(userid, androidid);

        HttpStatus status = HttpStatus.CREATED;
        if (family==null) { // 저장 실패
            status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()));
        }

        return ResponseEntity.status(status).body("");
    }


    // 유저 아이디 체크
    @GetMapping("/join/check/user") // https://~~?userid=123
    public ResponseEntity check_user(@RequestParam("userid") String userid){

        HeaderSetter headers = new HeaderSetter();
        if(customUserDetailService.exist_userid(userid)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }else{
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }
    }

}