package org.air.controller;

import org.air.config.CustomCode;
import org.air.config.HeaderSetter;
import org.air.entity.Messege;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.jwt.JwtTokenProvider;
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
 * upload: 일정 등록 <br>
 * delete: 일정 삭제 <br>
 * modify: 일정 수정 <br>
 * <br>
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // JPG 로부터 데이터 추출 후 저장
    @PostMapping("/upload")
    public ResponseEntity upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String token = request.getHeader("Authorization");
        String userid = jwtTokenProvider.getUserPk(token);
        HeaderSetter headerSetter = new HeaderSetter();

        try {
            List<Schedule> schedules = scheduleService.textrack(file.getInputStream());
            // 201 성공
            if (!schedules.isEmpty()) {
                List<Schedule> result = scheduleService.schedule_save(schedules, userid);

                if (result.isEmpty()) { // 저장 실패
                    return ResponseEntity
                            .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                            .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                            .body(new Messege(StatusEnum.SAVE_ERROR.getStatusCode(),
                                    StatusEnum.SAVE_ERROR.getMessage()));
                } else { // 저장 성공
                    return ResponseEntity
                            .ok()
                            .headers(headerSetter.haederSet(token, "SAVE!"))
                            .body(new Messege("201", ""));
                }
            } else {
                // textrack 실패
                return ResponseEntity
                        .status(Integer.parseInt(StatusEnum.TEXTRACK_EMPTY_ERROR.getStatusCode()))
                        .headers(headerSetter.haederSet(token, "plz, check your Image or Schedule sheet"))
                        .body(
                                new Messege(StatusEnum.TEXTRACK_EMPTY_ERROR.getStatusCode(),
                                        StatusEnum.TEXTRACK_EMPTY_ERROR.getMessage())
                        );
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(Integer.parseInt(StatusEnum.TEXTRACK_ERROR.getStatusCode()))
                    .headers(headerSetter.haederSet(token, ""))
                    .body(new Messege(StatusEnum.TEXTRACK_ERROR.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/modify")
    public ResponseEntity modify(HttpServletRequest request, @RequestBody Schedule schedule) {
        String token = request.getHeader("Authorization");
        HeaderSetter headerSetter = new HeaderSetter();
        CustomCode customCode = scheduleService.modify(schedule.getId(), schedule);

        return ResponseEntity
                .status(Integer.parseInt(customCode.getStatus().getStatusCode()))
                .headers(headerSetter.haederSet(token, "Success modify"))
                .body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        HeaderSetter headerSetter = new HeaderSetter();
        String userid = jwtTokenProvider.getUserPk(token);

        Boolean rst = scheduleService.delete(userid); //  Table all clear
        if (rst) {
            HttpHeaders headers = headerSetter.haederSet(token, "All clear your Schedules!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("");
        } else {
            HttpHeaders headers  = headerSetter.haederSet(token, "DELETE fail!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("");
        }
    }
}
