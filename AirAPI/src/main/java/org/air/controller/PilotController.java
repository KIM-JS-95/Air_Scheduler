package org.air.controller;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 사용자가 일반적으로 사용 가능한 기능 모음집 <br>
 * Function <br>
 * index: 일정 호출 <br>
 * getSchedules: 유저의 3일(Today + 2) 일정 획득 + 해당 지역의 날씨 <br>
 * <br>
 */

import org.air.config.HeaderSetter;
import org.air.entity.NationCode;
import org.air.entity.Schedule;
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
    private HeaderSetter headerSetter;
    // 메인 페이지
    @GetMapping("/home")
    public ResponseEntity index(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(token, "main page");
        return ResponseEntity.ok()
                .headers(header)
                .body("home");
    }

    @PostMapping("/getschedule") // today or specific date
    public ResponseEntity getSchedules(HttpServletRequest request, @RequestBody Map<String, String> requestBody) throws ParseException {
        String receivedDateTime = requestBody.get("dateTime");

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        String startDate = dateFormat.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(startDate));
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        String endDate = dateFormat.format(calendar.getTime());

        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(request.getHeader("Authorization"), "main page");
        List<Schedule> list = scheduleService.getSchedules(receivedDateTime, endDate);

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }

    @PostMapping("/gettodayschedule") // today or specific date
    public ResponseEntity gettodayschedule(HttpServletRequest request) throws ParseException {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        //String startDate = dateFormat.format(new Date());
        String startDate = "02Nov23";
        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(request.getHeader("Authorization"), "main page");
        List<Schedule> list = scheduleService.getTodaySchedules(startDate);

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }


    @GetMapping("/getnationcode") // today or specific date
    public ResponseEntity getnationcode(HttpServletRequest request) throws ParseException {

        HeaderSetter headers = new HeaderSetter();
        HttpHeaders header = headers.haederSet(request.getHeader("Authorization"), "main page");
        Map<String, Map<String, String>> list = scheduleService.getNationCode();

        return ResponseEntity.ok()
                .headers(header)
                .body(list);
    }

    @GetMapping("/show-schedule")
    public ResponseEntity showAllSchedules(HttpServletRequest request){
        String msg ="";
        List<Schedule> schedules = scheduleService.getALlSchedules();
        if(schedules.size()==0){
            msg="No Data";
        }else {
            msg="find All!";
        }
        HttpHeaders header =headerSetter.haederSet(request.getHeader("Authorization"),msg);
        return ResponseEntity.ok()
                .headers(header)
                .body(schedules);
    }

}
