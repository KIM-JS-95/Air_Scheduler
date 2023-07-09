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
import org.air.entity.Schedule;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @GetMapping("/schedule")
    public List<Schedule> getSchedules() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String s_date = simpleDateFormat.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(s_date));
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        String e_date = simpleDateFormat.format(calendar.getTime());
        List<Schedule> schedules = scheduleService.getSchedules(s_date, e_date);
        return schedules;
    }

}
