package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Schedule;
import org.air.entity.User;
import org.air.jwt.JwtAuthenticationFilter;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class PilotControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private CustomUserDetailService customUserDetailService;
    @MockBean
    private ScheduleService scheduleService;
    User user;
    String token;

    @BeforeEach
    public void init() {
        user = User.builder()
                .userid("001200")
                .name("tester")
                .build();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(new Date()));
    }


    @Test
    @DisplayName("Success main_page_access")
    public void 메인페이지에접속합니다() throws Exception {

        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        mvc.perform(get("/home")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("faile to main_page_access")
    public void 메인페이지에접속실패() throws Exception {
        mvc.perform(get("/home")
                        .header("Authorization", "fail token")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Today schedule")
    public void getTodaySchedules() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        mvc.perform(get("/getschedule_by_today")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("Date Schedule")
    public void getDateSchedules() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        mvc.perform(get("/getschedule_by_date")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("s_date", "01Nov23")
                        .param("e_date", "03Nov23")
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

}