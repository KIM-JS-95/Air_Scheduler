package org.air.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.air.config.CustomCode;
import org.air.config.HeaderSetter;
import org.air.entity.Messege;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.FcmServiceImpl;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private FcmServiceImpl fcmService;

    private final RestTemplate restTemplate = new RestTemplate();
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) throws IOException {
        String flaskUrl = "http://localhost:5000/process_image";
        String userid = jwtTokenProvider.getUserPk(token);
        HeaderSetter headerSetter = new HeaderSetter();

        if (userid.contains("test1")){
            return ResponseEntity.ok()
                    .headers(headerSetter.haederSet(token, "SAVE!"))
                    .body("");
        }

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Prepare body for the request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        // Create HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request
        ResponseEntity<byte[]> response = restTemplate.exchange(
                flaskUrl,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        // Check response status
        if (response.getStatusCode() == HttpStatus.OK) {

            try {
                List<Schedule> schedules = scheduleService.textrack(userid, file.getInputStream());

                if (!schedules.isEmpty()) { // 201 성공
                    List<Schedule> result = scheduleService.schedule_save(schedules, "test");

                    if (result.isEmpty()) { // 저장 실패
                        return ResponseEntity
                                .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                                .headers(headerSetter.haederSet(token, "SAVE ERROR"))
                                .body("");
                    } else { // 저장 성공
                        return ResponseEntity.ok()
                                .headers(headerSetter.haederSet(token, "SAVE!"))
                                .body("");
                    }
                } else {
                    // textrack 실패
                    return ResponseEntity
                            .status(Integer.parseInt(StatusEnum.TEXTRACK_EMPTY_ERROR.getStatusCode()))
                            .headers(headerSetter.haederSet(token, "plz, check your Image or Schedule sheet"))
                            .body("");
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(Integer.parseInt(StatusEnum.TEXTRACK_ERROR.getStatusCode()))
                        .headers(headerSetter.haederSet(token, ""))
                        .body("");
            }

        } else {
            throw new RuntimeException("Failed to process image");
        }
    }

    @PostMapping("/modify")
    public ResponseEntity modify(@RequestHeader("Authorization") String token, @RequestBody Schedule schedule) {
        HeaderSetter headerSetter = new HeaderSetter();
        CustomCode customCode = scheduleService.modify(schedule);

        return ResponseEntity
                .status(Integer.parseInt(customCode.getStatus().getStatusCode()))
                .headers(headerSetter.haederSet(token, "Success modify"))
                .body("");
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestHeader("Authorization") String token) {
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

    @PostMapping("/admin/fcm")
    public ResponseEntity FCM_All_User(@RequestHeader("Authorization") String token, @RequestBody Messege messege)
            throws FirebaseMessagingException {

        String headerMgs = (fcmService.sendMessageAll(messege)) ? "Complete transe Messeges" : "Faild transe Messeges";

        HeaderSetter headerSetter = new HeaderSetter();
        HttpHeaders headers  = headerSetter.haederSet(token, headerMgs);
        return ResponseEntity.ok()
                .headers(headers)
                .body("");
    }

}
