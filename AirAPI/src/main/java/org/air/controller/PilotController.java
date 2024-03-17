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
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
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

    // 메인 페이지
    @GetMapping("/home")
    public ResponseEntity index(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders header = headerSetter.haederSet(token, "main page");
        return ResponseEntity.ok() // 200
                .headers(header)
                .body("home");
    }

    @PostMapping("/getschedule") // today or specific date
    public ResponseEntity getSchedules(HttpServletRequest request, @RequestBody Map<String, String> requestBody) throws ParseException {
        String token = request.getHeader("Authorization");
        String receivedDateTime = requestBody.get("dateTime");

        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(token, "main page");
        List<Schedule> list = scheduleService.getSchedules(receivedDateTime);

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }

    // 사용 안함~~
    @GetMapping("/getschedule_by_date")
    public ResponseEntity getDateSchedules(@RequestParam("s_date") String sDate, @RequestParam("e_date") String eDate, HttpServletRequest request) throws ParseException {
        String token = request.getHeader("Authorization");
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        String startDate = sDate != null ? sDate : dateFormat.format(new Date());
        String endDate = eDate != null ? eDate : "";

        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(token, "main page");
        List<Schedule> list = scheduleService.getSchedulesBydate(startDate, endDate);

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }

    @PostMapping("/gettodayschedule") // today or specific date
    public ResponseEntity gettodayschedule(HttpServletRequest request) throws ParseException {
        String token = request.getHeader("Authorization");
        //SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        //String startDate = dateFormat.format(new Date());
        String startDate = "02Nov23";
        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(token, "main page");
        List<Schedule> list = scheduleService.getTodaySchedules(startDate);

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
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
        String token = request.getHeader("Authorization");
        System.out.println(token);
        List<Schedule> schedules = scheduleService.getAllSchedules();
        StatusEnum status = schedules.isEmpty() ? StatusEnum.No_DATA : StatusEnum.OK;

        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders header = headerSetter.haederSet(token, "");

        return ResponseEntity
                .status(Integer.parseInt(status.getStatusCode()))
                .headers(header)
                .body(schedules);
    }

}
