package org.AirAPI.config;

import org.AirAPI.entity.Messege;
import org.AirAPI.entity.StatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Configuration
public class HeaderSetter {

    public HttpHeaders haederSet(String token, String msg){


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("Authorization",token);
        headers.set("message", "login Success");
        headers.set("msg",msg);

        return headers;
    }
}
