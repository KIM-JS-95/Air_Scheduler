package org.air.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.air.config.CustomCode;
import org.air.config.HeaderSetter;
import org.air.entity.Messege;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.FcmServiceImpl;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 스케쥴 관리 모음집  <br>
 * Function <br>
 * upload: 일정 등록 <br>
 * delete: 일정 삭제 <br>
 * modify: 일정 수정 <br>
 * <br>
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private FcmServiceImpl fcmService;

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        String userid = jwtTokenProvider.getUserPk(token);
        HeaderSetter headerSetter = new HeaderSetter();
        if (userid.contains("test")){
            return ResponseEntity
                    .ok()
                    .headers(headerSetter.haederSet(token, "SAVE!"))
                    .body("");
        }
        int check = customUserDetailService.getSchedule_chk(userid);


        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        int month = Integer.parseInt(dateFormat.format(today));


        if (check == month) { // 이미 저장된 일정
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.TEXTRACK_already_save.getStatusCode()))
                    .headers(headerSetter.haederSet(token, "Already saved your schedules"))
                    .body("");
        }
        try {
            List<Schedule> schedules = scheduleService.textrack(userid, file.getInputStream());

            if (!schedules.isEmpty()) { // 201 성공
                List<Schedule> result = scheduleService.schedule_save(schedules, userid);

                if (result.isEmpty()) { // 저장 실패
                    return ResponseEntity
                            .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                            .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                            .body("");
                } else { // 저장 성공
                    return ResponseEntity.ok()
                            .headers(headerSetter.haederSet(token, "SAVE!"))
                            .body("");
                }
            } else {
                // textrack 실패
                return ResponseEntity
                        .status(Integer.parseInt(StatusEnum.TEXTRACK_EMPTY_ERROR.getStatusCode()))
                        .headers(headerSetter.haederSet(token, "plz, check your Image or Schedule sheet"))
                        .body("");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.TEXTRACK_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, ""))
                    .body("");
        }
    }

    @PostMapping("/modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody Schedule schedule) {
        HeaderSetter headerSetter = new HeaderSetter();
        CustomCode customCode = scheduleService.modify(schedule);

        return ResponseEntity
                .status(Integer.parseInt(customCode.getStatus().getStatusCode()))
                .headers(headerSetter.haederSet(token, "Success modify"))
                .body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestHeader("Authorization") String token) {
        HeaderSetter headerSetter = new HeaderSetter();
        String userid = jwtTokenProvider.getUserPk(token);

        Boolean rst = scheduleService.delete(userid); //  Table all clear
        if (rst) {
            HttpHeaders headers = headerSetter.haederSet(token, "All clear your Schedules!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("");
        } else {
            HttpHeaders headers  = headerSetter.haederSet(token, "DELETE fail!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("");
        }
    }

    @PostMapping("/admin/fcm")
    public ResponseEntity FCM_All_User(@RequestHeader("Authorization") String token, @RequestBody Messege messege)
            throws FirebaseMessagingException {

        String headerMgs = (fcmService.sendMessageAll(messege)) ? "Complete transe Messeges" : "Faild transe Messeges";

        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers  = headerSetter.haederSet(token, headerMgs);
        return ResponseEntity.ok()
                .headers(headers)
                .body("");
    }

}
