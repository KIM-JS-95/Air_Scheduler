package org.air.controller;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 사용자가 일반적으로 사용 가능한 기능 모음집 <br>
 * Function <br>
 * index: 일정 호출 <br>
 * getTodaySchedules: 당일 모든 일정 획득 <br>
 * getDateSchedules : 요청일 스케줄 가져오기 <br>
 * showAllSchedules : 모든 스케쥴 가져오기 <br>
 * <br>
 */

import org.air.config.HeaderSetter;
import org.air.entity.Messege;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.jwt.JwtTokenProvider;
import org.air.service.ScheduleService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;


@RestController
public class PilotController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private  JwtTokenProvider jwtTokenProvider;

    @PostMapping("/getschedule")
    public ResponseEntity getSchedules(HttpServletRequest request,
                                       @RequestBody Map<String, String> requestBody) throws ParseException {

        String token = request.getHeader("Authorization");
        String receivedDateTime = requestBody.get("dateTime");

        HeaderSetter headers = new HeaderSetter();
        List<Schedule> list = scheduleService.getTodaySchedules(receivedDateTime);

        if(list.isEmpty()){
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet(token, StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }else {
            return ResponseEntity.ok()
                    .headers(headers.haederSet(token, ""))
                    .body(list);
        }
    }

    @PostMapping("/gettodayschedule") // today or specific date
    public ResponseEntity gettodayschedule(HttpServletRequest request) throws ParseException {
        HeaderSetter headers = new HeaderSetter();

        String token = request.getHeader("Authorization");
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        String startDate = dateFormat.format(new Date());
        //String startDate = "01Nov23";

        List<Schedule> list = scheduleService.getTodaySchedules(startDate);

        // 상태코드는 status 에 넣고 나머지는 헤더에 넣자
        if(list.isEmpty()){
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet(token, StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }else {
            return ResponseEntity.ok()
                    .headers(headers.haederSet(token, ""))
                    .body(list);
        }
    }


    @GetMapping("/getnationcode") // today or specific date
    public ResponseEntity getnationcode(HttpServletRequest request) throws ParseException {
        String token = request.getHeader("Authorization");
        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(token, "main page");
        Map<String, Map<String, String>> list = scheduleService.getNationCode();

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }

    @GetMapping("/showschedule")
    public ResponseEntity showAllSchedules(HttpServletRequest request) {
        HeaderSetter headers = new HeaderSetter();
        String token = request.getHeader("Authorization");
        String userid = jwtTokenProvider.getUserPk(token);
        List<Schedule> schedules = scheduleService.getAllSchedules(userid);

        if(schedules.isEmpty()){
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                    .headers(headers.haederSet(token, StatusEnum.NOT_FOUND.getMessage()))
                    .body("");
        }else {
            return ResponseEntity.ok()
                    .headers(headers.haederSet(token, ""))
                    .body(schedules);
        }
    }
}
