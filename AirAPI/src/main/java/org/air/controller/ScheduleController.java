package org.air.controller;

import org.air.config.HeaderSetter;
import org.air.entity.Schedule;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity upload_schedules(@RequestPart MultipartFile file,
                                        HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers = null;
        try {
            // 텍스트 추출 시작
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());
            System.out.println("schedules size : " + schedules.size());
            boolean result = scheduleService.schedule_save(schedules);
            String msg = result ? "save" : "save errors";
            headers = headerSetter.haederSet("save result", msg);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body("Save Success!");
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
