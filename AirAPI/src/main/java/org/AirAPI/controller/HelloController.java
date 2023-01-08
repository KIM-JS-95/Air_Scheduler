package org.AirAPI.controller;


import org.AirAPI.Entity.Messege;
import org.AirAPI.Entity.Schedule;
import org.AirAPI.Entity.StatusEnum;
import org.AirAPI.Service.ScheduleService;
import org.AirAPI.config.AWStextrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.textract.TextractClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String hello() {
        return "hello";
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

        // 파일은 s3 에 저장하며 이후 스케쥴 삭제 기능을 추가한다.
        try {
            scheduleService.save(file.getInputStream());
        }catch (Exception e){
            return "Can't save";
        }

        return "save";
    }
}
