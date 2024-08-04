package org.air.controller;

import org.air.config.HeaderSetter;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class CropController {

    @Autowired
    private ScheduleService scheduleService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000/process_image";

    @PostMapping("/process_image")
    public ResponseEntity<String> processImage(@RequestParam("file") MultipartFile file) throws IOException {

        HeaderSetter headerSetter = new HeaderSetter();

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
            InputStream inputStream = new ByteArrayInputStream(response.getBody());
            System.out.println(inputStream);

            try {
                List<Schedule> schedules = scheduleService.textrack("test", file.getInputStream());

                if (!schedules.isEmpty()) { // 201 성공
                    List<Schedule> result = scheduleService.schedule_save(schedules, "test");

                    if (result.isEmpty()) { // 저장 실패
                        return ResponseEntity
                                .status(Integer.parseInt(StatusEnum.SAVE_ERROR.getStatusCode()))
                                .headers(headerSetter.haederSet("token", "SAVE ERROR"))
                                .body("");
                    } else { // 저장 성공
                        return ResponseEntity.ok()
                                .headers(headerSetter.haederSet("token", "SAVE!"))
                                .body("");
                    }
                } else {
                    // textrack 실패
                    return ResponseEntity
                            .status(Integer.parseInt(StatusEnum.TEXTRACK_EMPTY_ERROR.getStatusCode()))
                            .headers(headerSetter.haederSet("token", "plz, check your Image or Schedule sheet"))
                            .body("");
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(Integer.parseInt(StatusEnum.TEXTRACK_ERROR.getStatusCode()))
                        .headers(headerSetter.haederSet("token", ""))
                        .body("");
            }

        } else {
            throw new RuntimeException("Failed to process image");
        }
    }
}
