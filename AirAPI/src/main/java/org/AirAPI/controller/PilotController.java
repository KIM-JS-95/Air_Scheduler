package org.AirAPI.controller;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 사용자가 일반적으로 사용 가능한 기능 모음집 <br>
 * Function <br>
 * index: 일정 호출 <br>
 * getSchedules: 유저의 3일(Today + 2) 일정 획득 + 해당 지역의 날씨 <br>
 * <br>
 */

import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PilotController {

    @Autowired
    private ScheduleService scheduleService;

    // 메인 페이지
    @GetMapping("/home")
    public HttpHeaders index(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        HeaderSetter headers = new HeaderSetter();
        HttpHeaders response = headers.haederSet(token, "main page");
        return response;
    }

    // 해당 유저의 고유 기본키값을 통해 데이터를 획득
    @GetMapping("/schedule/{id}")
    public List<Schedule> getSchedules(@PathVariable int id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        simpleDateFormat.format(new Date());
        return scheduleService.getSchedules(id);
    }
}
