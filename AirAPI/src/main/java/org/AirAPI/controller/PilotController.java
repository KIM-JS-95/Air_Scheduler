package org.AirAPI.controller;


import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class PilotController {

    @Autowired
    private ScheduleService scheduleService;

    // 메인 페이지
    @GetMapping("/home")
    public ResponseEntity index(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        HeaderSetter headers = new HeaderSetter();
        ResponseEntity response = headers.haederSet(token, "main page", HttpStatus.OK);
        return response;
    }

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestPart MultipartFile file, HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        ResponseEntity response = null;
        try {
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());
            boolean result = scheduleService.schedule_save(schedules);
            if (result) {
                response = headerSetter.haederSet(
                        request.getHeader("Authorization"), "save!", HttpStatus.OK);
            } else {
                response = headerSetter.haederSet(
                        request.getHeader("Authorization"), "save error!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

    // 해당 유저의 고유 기본키값을 통해 데이터를 획득
    @GetMapping("/schedule/{id}")
    public List<Schedule> getSchedules(@PathVariable int id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        simpleDateFormat.format(new Date());
        return scheduleService.findData(id);
    }
}
