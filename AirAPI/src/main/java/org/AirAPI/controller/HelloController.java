package org.AirAPI.controller;


import org.AirAPI.entity.Messege;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.StatusEnum;
import org.AirAPI.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public ResponseEntity hello() {

        // 쿠키 인증 클래스 만들기
        Messege message = new Messege();
        message.setStatus(StatusEnum.OK);
        message.setMessage("");
        message.setData("헤더입니다.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("message", "cool!");

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @GetMapping("/1")
    public String hello2(HttpServletRequest request) {
        String headers = request.getHeader("message");
        System.out.println(headers);
        return headers;
    }

    @PostMapping("/save")
    public ResponseEntity<Messege> mainSave(@RequestBody List<Schedule> schedules) {

        // 쿠키 인증 클래스 만들기
        Messege message = new Messege();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(schedules);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("message", "성공 코드");
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }


    @PostMapping("/jpg")
    public String upload(@RequestPart MultipartFile file) throws IOException {

        try {
            scheduleService.save(file.getInputStream());
        }catch (Exception e){
            return "Can't save";
        }
        return "save";
    }
}
