package org.AirAPI.controller;

import org.AirAPI.config.HeaderSetter;
import org.AirAPI.jwt.JwtAuthenticationFilter;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.repository.SchduleRepository;
import org.AirAPI.service.CustomUserDetailService;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private SchduleRepository schduleRepository;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    public void login_test() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.put("email", Collections.singletonList("aabbcc@gmail.com"));

        mvc.perform(post("/login")
                .params(parameters).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void logout_test() throws Exception {
        List<String> roles = new ArrayList<>();
        String token = jwtTokenProvider.createToken("aabbcc@gmail.com",roles);

        mvc.perform(post("/2")
                        .header("Authorization", token))
                .andExpect(content().string(token));
    }
}