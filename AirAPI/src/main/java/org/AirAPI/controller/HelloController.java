package org.AirAPI.controller;


import org.AirAPI.Entity.Messege;
import org.AirAPI.Entity.Schedule;
import org.AirAPI.Entity.StatusEnum;
import org.AirAPI.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String hello(){
        return "hello";
    }

    @PostMapping("/save")
    public ResponseEntity<Messege> mainSave(@RequestBody List<Schedule> schedules){

        Messege message = new Messege();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(schedules);

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        return new ResponseEntity<>(message,headers,HttpStatus.OK);
    }
}
