package org.AirAPI.controller;


import org.AirAPI.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String hello(){
        return "hello";
    }
}
