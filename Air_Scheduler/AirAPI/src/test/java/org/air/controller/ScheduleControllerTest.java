package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.air.config.CustomCode;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.air.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@Import(ScheduleController.class)
@WebMvcTest(ScheduleController.class)
@ContextConfiguration(classes = {JwtTokenProvider.class})
class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private String token;

    @BeforeEach
    public void init() {
        user = User.builder()
                .userid("001200")
                .name("침착맨")
                .build();

        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(new Date()));
    }

    @Test
    @Disabled
    @DisplayName("textrack_test")
    public void jpg_save_test() throws Exception {
        when(customUserDetailService.loadUserById(user.getUserid()))
                .thenReturn(user);

        final String filePath = "resources\\static\\img\\November.jpg"; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile img = new MockMultipartFile(
                "file", "sample.jpg", "jpg", fileInputStream);

        mvc.perform(multipart("/upload")
                        .file(img)
                        .header("Authorization", token)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpload() throws Exception {
        // Your test logic here
        mvc.perform(post("/upload"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testModify() throws Exception {
        Schedule schedule = new Schedule();
        schedule.setId(1L);

        when(scheduleService.modify(schedule.getId(), schedule))
                .thenReturn(new CustomCode(StatusEnum.OK));

        mvc.perform(post("/modify")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(schedule))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void testDelete() throws Exception {
        mvc.perform(delete("/delete")
                        .header("Authorization", token)
                        .with(csrf()
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
