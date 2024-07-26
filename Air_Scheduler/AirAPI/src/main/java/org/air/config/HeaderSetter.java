package org.air.config;

import org.air.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;

@Configuration
public class HeaderSetter {

    public HttpHeaders haederSet(String token, String msg){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("Authorization", token);
        headers.set("message", msg);
        return headers;
    }
}
