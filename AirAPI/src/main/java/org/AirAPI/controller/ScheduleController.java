package org.AirAPI.controller;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 스케쥴 관리 모음집  <br>
 * Function <br>
 * upload_schedules: 일정 등록 <br>
 * delete_schedules: 일정 삭제 <br>
 * modify_schedules: 일정 수정 <br>
 * <br>
 */

import org.AirAPI.config.HeaderSetter;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;


    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload_schedules(@RequestPart MultipartFile file, HttpServletRequest request) {
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
}
