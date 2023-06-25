package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.config.HeaderSetter;
import org.air.config.SecurityConfig;
import org.air.entity.Authority;
import org.air.entity.Messege;
import org.air.entity.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
@Import(UserController.class)
@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {SecurityConfig.class, HeaderSetter.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void userset() {
        String userid = "001200";
        String username = "침착맨";

        user = User.builder()
                .userId(userid)
                .name(username)
                .build();
        Authority userAuthority = new Authority("USER");
        user.addAuthority(userAuthority);
    }

    @Test
    @DisplayName("회원가입")
    @WithMockUser
    public void join() throws Exception {

        user = User.builder()
                .userId("001200")
                .name("침착맨")
                .build();

        String user_ad_String = objectMapper.writeValueAsString(user);

        given(customUserDetailService.save(user)).willReturn(user);
        given(jwtUtil.createToken("침착맨", "20220404"))
                .willReturn("header.payload.signature");

        mvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(user_ad_String)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인")
    public void login_test() throws Exception {
        when(customUserDetailService.loadUserById("001200")).thenReturn(user);

        String jsonString = "{\"userId\": \"001200\",\"name\": \"침착맨\"}";
        mvc.perform(post("/login")
                        .content(jsonString)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().string("message", "login Success"))
                .andDo(print());

    }
    @Test
    @DisplayName("로그아웃 테스트")
    public void logout_test() throws Exception {
        mvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andReturn();
    }

}