package org.AirAPI.controller;

import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 스케쥴 관리 모음집  <br>
 * Function <br>
 *  upload_schedules: 일정 등록 <br>
 *  delete_schedules: 일정 삭제 <br>
 *  modify_schedules: 일정 수정 <br>
 * <br>
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public HttpHeaders upload_schedules(@RequestPart MultipartFile file, HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders response = null;
        try {
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());
            boolean result = scheduleService.schedule_save(schedules);
            if (result) {
                response = headerSetter.haederSet(
                        request.getHeader("Authorization"), "save!");
            } else {
                response = headerSetter.haederSet(
                        request.getHeader("Authorization"), "save error!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

    public ResponseEntity modify_schedules(){
        ResponseEntity header =null;
        return header;
    }


    public ResponseEntity delete_schedules(){
        ResponseEntity header =null;
        return header;
    }
}
