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
     *  upload: 일정 등록 <br>
     *  delete: 일정 삭제 <br>
     *  modify: 일정 수정 <br>
 * <br>
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file,
                                        HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers = null;
        try {
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());
            System.out.println("schedules size : " + schedules.size());
            if(schedules.size()!=0) {
                boolean result = scheduleService.schedule_save(schedules);
                String msg = result ? "save" : "save errors";
                headers = headerSetter.haederSet("save result", msg);
            }else{
                headers = headerSetter.haederSet("save result", "No data");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body("Save Success!");
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity modify(@PathVariable("id")Long id, @RequestBody Schedule schedule){
        HttpHeaders header = new HttpHeaders();
        Schedule schedule1= scheduleService.modify(id, schedule);
        return ResponseEntity.ok()
                .headers(header)
                .body(schedule1);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpServletRequest request){
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers = null;
        Boolean rst = scheduleService.delete(); //  Table all clear
        if(rst){
            headers = headerSetter.haederSet(request.getHeader("Authorization"), "All clear your Schedules!");
        }else{
            headers = headerSetter.haederSet(request.getHeader("Authorization"), "clear fail!");
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body("");
    }
}
