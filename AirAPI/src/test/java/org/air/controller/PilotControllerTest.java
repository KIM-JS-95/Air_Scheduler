package org.air.controller;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Authority;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.entity.User;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        Authority authority = Authority.builder()
                .id(1L)
                .authority("USER")
                .build();

        user = User.builder()
                .userid("001200")
                .name("tester")
                .authority(authority)
                .build();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(new Date()));
    }

    @Test
    @DisplayName("OK: getSchedules")
    public void getSchedules_OK() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);
        List<Schedule> schedule = new ArrayList<>();
        schedule.add(Schedule.builder()
                .id(1L)
                .build());

        when(scheduleService.getSchedules(any())).thenReturn(schedule);

        mvc.perform(post("/getschedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateTime\": \"01Nov23\"}") // Corrected JSON string
                ).andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("FAIL: getSchedules")
    public void getSchedules_FAIL() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        when(scheduleService.getSchedules(any())).thenReturn(new ArrayList<>());

        mvc.perform(post("/getschedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateTime\": \"01Nov23\"}") // Corrected JSON string
                ).andExpect(status().is(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode())))
                .andDo(print());
    }

    @Test
    @DisplayName("OK: all schedules")
    public void getAllSchedules_OK() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);
        List<Schedule> schedule = new ArrayList<>();
        schedule.add(Schedule.builder()
                .id(1L)
                .build());

        when(scheduleService.getAllSchedules()).thenReturn(schedule);

        mvc.perform(get("/showschedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("FAIL: all schedules")
    public void getAllSchedules_FAIL() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        List<Schedule> schedule = new ArrayList<>();

        when(scheduleService.getAllSchedules()).thenReturn(schedule);

        mvc.perform(
                        get("/showschedule")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status()
                                .is(Integer.parseInt(StatusEnum.NOT_FOUND.getStatusCode()))
                );
    }

    @Test
    @DisplayName("OK: Today schedule")
    public void getTodaySchedules_OK() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        when(scheduleService.getTodaySchedules("01Nov23"))
                .thenReturn(List.of(Schedule.builder()
                        .id(1L)
                        .build()));

        mvc.perform(post("/gettodayschedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("FALI:Today schedule")
    public void getTodaySchedules_FAIL() throws Exception {
        when(customUserDetailService.loadUserById(jwtTokenProvider.getUserPk(token)))
                .thenReturn(user);

        when(scheduleService.getTodaySchedules("01Nov23"))
                .thenReturn(new ArrayList<>());

        mvc.perform(post("/gettodayschedule")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(103))
                .andDo(print());
    }


}