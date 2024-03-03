package org.air.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class HeaderSetter {

    public HttpHeaders haederSet(HttpServletRequest request, String msg){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        headers.set("Authorization",request.getHeader("Authorization"));
        headers.set("message", msg);
        return headers;
    }
}
