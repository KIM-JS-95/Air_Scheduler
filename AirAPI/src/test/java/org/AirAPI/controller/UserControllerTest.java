package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.User;
import org.AirAPI.repository.ScheduleRepository;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.AirAPI.service.CustomUserDetailService;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ScheduleRepository schduleRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private ScheduleService scheduleService;

    User user = null;

    @BeforeAll
    public void userset() {
        String userid = "001200";
        String username = "침착맨";

        user = User.builder()
                .userId(userid)
                .name(username)
                .build();
    }

    @Test
    @DisplayName("회원가입")
    public void join() throws Exception {

        String user_ad_String = objectMapper.writeValueAsString(user);

        mvc.perform(post("/join")
                        .content(user_ad_String)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(user.toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    public void login_test() throws Exception {
        when(customUserDetailService.loadUserById("001200")).thenReturn(user);

        String jsonString = "{\"userId\": \"001200\",\"name\": \"침착맨\"}";
        mvc.perform(post("/login")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logout_test() throws Exception {
        mvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().is3xxRedirection());
    }

}