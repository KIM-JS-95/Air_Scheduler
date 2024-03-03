package org.air.controller;

import org.air.config.CustomCode;
import org.air.config.HeaderSetter;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author : KIM JAE SEONG <br>
 * Content: 스케쥴 관리 모음집  <br>
 * Function <br>
 * upload: 일정 등록 <br>
 * delete: 일정 삭제 <br>
 * modify: 일정 수정 <br>
 * <br>
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        try {
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());

            if (!schedules.isEmpty()) {
                List<Schedule> result = scheduleService.schedule_save(schedules);
                String msg = (!result.isEmpty()) ? "save" : "save errors";

                return ResponseEntity.ok()
                        .headers(headerSetter.haederSet(request, msg))
                        .body("Save Success!");
            } else {
                return ResponseEntity
                        .status(Integer.parseInt(StatusEnum.No_DATA.getStatusCode()))
                        .headers(headerSetter.haederSet(request, "No data"))
                        .body("");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.No_DATA.getStatusCode()))
                    .headers(headerSetter.haederSet(request, e.getMessage()))
                    .body("");
        }
    }

    @PostMapping("/modify")
    public ResponseEntity modify(HttpServletRequest request, @RequestBody Schedule schedule) {
        HeaderSetter headerSetter = new HeaderSetter();
        CustomCode customCode = scheduleService.modify(schedule.getId(), schedule);

        return ResponseEntity
                .status(Integer.parseInt(customCode.getStatus().getStatusCode()))
                .headers(headerSetter.haederSet(request,"Success modify"))
                .body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpServletRequest request) {
        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers = null;

        Boolean rst = scheduleService.delete(); //  Table all clear
        if (rst) {
            headers = headerSetter.haederSet(request, "All clear your Schedules!");
        } else {
            headers = headerSetter.haederSet(request, "clear fail!");
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body("");
    }
}
