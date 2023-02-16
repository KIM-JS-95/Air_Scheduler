package org.AirAPI.config;

import org.AirAPI.entity.Messege;
import org.AirAPI.entity.StatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HeaderSetter {

    public ResponseEntity HaederSet(String token){

        // 쿠키 인증 클래스 만들기
        Messege message = new Messege();
        message.setStatus(StatusEnum.OK);
        message.setMessage("");
        message.setData("헤더입니다.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("Authorization",token);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
