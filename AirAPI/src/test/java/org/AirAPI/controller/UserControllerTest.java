package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.config.SecurityConfig;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtAuthenticationFilter;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.repository.SchduleRepository;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.AirAPI.service.CustomUserDetailService;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SecurityConfig securityConfig;


    @MockBean
    private SchduleRepository schduleRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("회원가입")
    public void join() throws Exception {
        final String BIRTH = "001200";
        final String EMAIL = "aabbcc@gmail.com";
        final String NICKNAME = "침착맨";
        final Long SEQUENCEID = Long.valueOf(1);

        User user = User.builder()
                .userEmail(EMAIL)
                .userBirth(BIRTH)
                .userNickname(NICKNAME)
                .userSequenceId(SEQUENCEID)
                .build();

        String content = objectMapper.writeValueAsString(user);

        mvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(content().string(user.toString()));
    }

    @Test
    @Disabled
    public void login_test() throws Exception {
        final String BIRTH = "001200";
        final String EMAIL = "aabbcc@gmail.com";
        final String NICKNAME = "침착맨";
        final Long SEQUENCEID = Long.valueOf(1);

        User user = User.builder()
                .userEmail(EMAIL)
                .userBirth(BIRTH)
                .userNickname(NICKNAME)
                .userSequenceId(SEQUENCEID)
                .build();

        given(customUserDetailService.test_save(user)).willReturn(user);

        String jsonString = "{\"userEmail\": \"aabbcc@gmail.com\",\"userNickname\": \"침착맨\"}";
        mvc.perform(post("/login")
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logout_test() throws Exception {
        List<String> roles = new ArrayList<>();
        mvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().is3xxRedirection());
    }

}