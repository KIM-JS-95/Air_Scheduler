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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private AWStextrack awstextrack;

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @PostMapping("/save")
    public ResponseEntity<Messege> mainSave(@RequestBody List<Schedule> schedules) {

        Messege message = new Messege();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(schedules);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("message", "성공 코드");
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    //  jpg 데이터를 획득 후 textrack 을 실행한다.
    public void AWS_textrack() {

        Region region = Region.US_EAST_2;
        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        String source = "static/img/sample.jpg";
        awstextrack.analyzeDoc(textractClient, source);
    }
}
