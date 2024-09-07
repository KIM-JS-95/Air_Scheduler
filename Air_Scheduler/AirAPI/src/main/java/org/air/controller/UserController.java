package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.config.HeaderSetter;
import org.air.entity.*;
import org.air.entity.DTO.TemppilotcodeDAO;
import org.air.entity.DTO.UserDTO;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.EmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity server_test() {

        String notice = "비행일정관리앱 개인정보 처리방침\n" +
                "기장님들의 개인정보는 서비스의 원활한 제공을 위하여 수집됩니다. \n" +
                "\n" +
                "\n" +
                "1. 개인정보의 처리 목적\n" +
                "\n" +
                "비행일정관리앱는 서비스를 원활하게 제공하고 더욱 향상된 이용자 경험을 드리기 위해 필요한 여러분의 \n" +
                "비행일정을 수집합니다. 여러분이 설치 시 동의한 권한에 따라 서비스의 기본 기능이나 여러 특화된\n" +
                "기능을 제공하기 위해서 연락처 및 일정 정보가 수집되며 서비스 외에 다른 곳에 사용하지 않음을 알려드립니다.\n" +
                "\n" +
                "2. 처리하는 개인정보 항목\n" +
                "\n" +
                "비행일정관리앱이 더 나은 서비스를 제공해 드리기 위해 수집하는 여러분의 개인정보는 아래와 같습니다.\n" +
                "비행일정관리앱은 여러분이 서비스에 처음 가입할 때 또는 서비스를 이용하는 과정에서 홈페이지 또는 \n" +
                "서비스 내 개별 어플리케이션이나 프로그램 등을 통하여 여러분의 전화번호, 단말기 기타기기정보 (개별 기기, 브라우저 또는 앱과 관련된 식별자입니다. IMEI 번호, MAC 주소), 스마트폰 등 단말기 식별번호\n" +
                "연락처, 메일, 비행 스케쥴 등이 선택적으로 수집됩니다.\n" +
                "\n" +
                "비행일정관리앱는 여러분의 별도 동의가 있는 경우나 법령에 규정된 경우를 제외하고는 여러분의 개인정보\n" +
                "를 절대 제3자에게 제공하지 않습니다.\n" +
                "비행일정은 매달 일정 기간이 지난 경우 지난 전달의 일절은 모두 삭제관리합니다.\n" +
                "\n" +
                "[개인정보보호 책임자]\n" +
                "이름: KIM-JS-95 연락처: 고객센터, [baugh2487@gmail.com]\n" +
                " \n" +
                "비행일정관리앱은 법률이나 서비스의 변경사항을 반영하기 위한 목적 등으로 개인정보 처리방침을 수정할\n" +
                "수 있습니다. 개인정보 처리방침이 변경되는 경우 소프트파워는 변경 사항을 게시하며, 변경된 개인정보\n" +
                "처리방침은 게시한 날로부터 7일 후부터 효력이 발생합니다. 하지만, 피치 못하게 여러분의 권리에 \n" +
                "중요한 변경이 있을 경우 변경될 내용을 30일 전에 미리 알려드리겠습니다.\n" +
                "\n" +
                "일자: 2024년 08월 04일\n" +
                "비행일정관리앱에서 제공하는 서비스에 대한 개인정보 처리방침 입니다.";
        return ResponseEntity.ok()
                .headers(headerSetter.haederSet("", "login Success"))
                .body(notice);
    }


    @GetMapping("/loginauto")
    public ResponseEntity loginAuto(@RequestParam String androidid) {

        User user = customUserDetailService.find_androidId(androidid);

        if (user == null || user.getAutologin()==0) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        }else { // 자동 로그인 실행
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(date));
            customUserDetailService.token_save(user, token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json"));
            headers.set("auth_level", user.getAuthority().getAuthority());
            headers.set("Authorization", token);
            headers.set("message", "login Success");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(user);
        }
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO user) throws MessagingException, IOException {
        User member = customUserDetailService.loadUserByUser(user);
        if (member == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("");
        } else if (member.getUserid().contains("test")) {
            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));
            customUserDetailService.token_save(member, token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json"));
            headers.set("auth_level", member.getAuthority().getAuthority());
            headers.set("Authorization", token);
            headers.set("message", "login Success");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(member);

        } else { // 안드로이드 ID값이 다르면? -> 임시 계정 테이블에 저장 -> 이메일로 전달
            if (!user.getAndroidid().equals(member.getAndroidid())) {
                TemppilotcodeDAO temppilotcodeDAO = TemppilotcodeDAO.builder()
                        .email(member.getEmail())
                        .username(member.getName())
                        .userid(member.getUserid())
                        .androidid(user.getAndroidid())
                        .build();

                customUserDetailService.login_check(temppilotcodeDAO); // 임시저장
                emailService.sendLoginCautionMail(member.getName(), member.getEmail(), member.getUserid(), user.getAndroidid());
                return ResponseEntity.status(Integer.parseInt(StatusEnum.DEVICE_NOT_MATCH.getStatusCode()))
                        .headers(headerSetter.haederSet("", StatusEnum.DEVICE_NOT_MATCH.getMessage()))
                        .body("");
            }

            Date date = new Date();
            SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
            String token = jwtTokenProvider.createToken(member.getUserid(), access_time.format(date));
            customUserDetailService.token_save(member, token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json"));
            headers.set("auth_level", member.getAuthority().getAuthority());
            headers.set("Authorization", token);
            headers.set("message", "login Success");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(member);
        }
    }

    @PostMapping("/user_modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody User user) {

        String user_string = jwtTokenProvider.getUserPk(token); // body: userid
        User member = customUserDetailService.modify(user, user_string);
        if (member != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json"));
            headers.set("auth_level", member.getAuthority().getAuthority());
            headers.set("Authorization", token);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(member);
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
        member.put("password", user.getAuthority());

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
                    .headers(headerSetter.haederSet(null, "logout Success"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "logout fail"))
                    .body("");
        }
    }

    @GetMapping("/user/delete")
    public ResponseEntity remove_user(@RequestHeader("Authorization") String token) {
        String user_string = jwtTokenProvider.getUserPk(token);

        if (customUserDetailService.delete_user(user_string)) {
            return ResponseEntity.status(HttpStatus.valueOf(StatusEnum.DELETE_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, "delete fail"))
                    .body("");
        } else {
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(null, "delete Success"))
                    .body("");
        }
    }


    // ----------------------------------- 회원가입 권한 -----------------------------------

    // 회원가입 이전 임시 저장 -> 해당 유저에게 이메일을 전달후(랜덤키 전달) 회원가입 처리 : <userid>@jejuair.net
    @PostMapping("/join/save/pilotcode")
    public ResponseEntity join(@RequestBody TemppilotcodeDAO temppilotcode) {
        Temppilotcode result = customUserDetailService.save_pilotcode(temppilotcode);
        if (result != null) {
            boolean emailSent = emailService.sendTokenMail(result.getEmail(), result.getRandomkey());
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
    public ResponseEntity join_pilot_fin(@RequestBody UserDTO user) { // password, randomkey, androidid
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
                .androidid(user.getAndroidid())
                .build();
        customUserDetailService.processPilotcode(temppilotcodeDAO); // 임시저장

        String mgs = user.getName() + "님과 일정을 공유합니다.";
        emailService.sendMail(user.getUserid(), user1.getName(), user.getName(), user1.getEmail(), mgs, user.getAndroidid());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }


    @GetMapping("/join/save/family/fin")
    public ResponseEntity join_family_fin(@RequestParam("userid") String userid,
                                          @RequestParam("androidid") String androidid) {
        User family = customUserDetailService.saveFamily(userid, androidid);

        HttpStatus status = HttpStatus.CREATED;
        if (family == null) { // 저장 실패
            status = HttpStatus.valueOf(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()));
        }

        return ResponseEntity.status(status).body("");
    }


    // 유저 아이디 체크
    @GetMapping("/join/check/user") // https://~~?userid=123
    public ResponseEntity check_user(@RequestParam("userid") String userid) {

        HeaderSetter headers = new HeaderSetter();
        if (customUserDetailService.exist_userid(userid)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        } else {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet("", StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }
    }

}