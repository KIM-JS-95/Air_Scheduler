package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.config.HeaderSetter;
import org.air.config.SecurityConfig;
import org.air.entity.Authority;
import org.air.entity.Messege;
import org.air.entity.User;
import org.air.jwt.JwtAuthenticationFilter;
import org.air.jwt.JwtTokenProvider;
import org.air.repository.ScheduleRepository;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.air.service.CustomUserDetailService;
import org.air.service.ScheduleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(UserController.class)
@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {SecurityConfig.class, HeaderSetter.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private HeaderSetter headerSetter;
    @MockBean
    private Messege messege;
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

    @MockBean
    private JwtTokenProvider jwtUtil;

    User user = null;

    @BeforeAll
    public void init() {
        String userid = "001200";
        String username = "침착맨";
        user = User.builder()
                .userid(userid)
                .name(username)
                .build();
    }

    @Test
    @DisplayName("Sign Up")
    public void join() throws Exception {
        Authority authority = Authority.builder()
                .authority("ROLE_USER")
                .build();

        user.setAuthority(authority);

        // 객체를 JSON으로 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        //String user_ad_String = objectMapper.writeValueAsString(user);
/*
        mvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user_ad_String)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userid").value("001200"));
*/
    }

    @Test
    @DisplayName("login Test")
    public void login_test() throws Exception {
        String jsonString = "{\"userid\": \"001200\",\"name\": \"침착맨\"}";
        mvc.perform(post("/login")
                        .content(jsonString)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Logout Test")
    public void logout_test() throws Exception {

        // 로그아웃 > 유저 refresh 테이블에 해당 유저 토큰 삭제기능 추가
        mvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}