package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.config.HeaderSetter;
import org.AirAPI.config.SecurityConfig;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.repository.ScheduleRepository;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.AirAPI.service.CustomUserDetailService;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@Import(UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityConfig securityConfig;

    @MockBean
    private HeaderSetter headerSetter;
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
    }

    @Test
    @DisplayName("회원가입")
    @WithMockUser
    public void join() throws Exception {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        String user_ad_String = objectMapper.writeValueAsString(user);
        user.setAuthorities(authorities);

        given(jwtUtil.createToken("침착맨", "20220404"))
                .willReturn("header.payload.signature");
        given(customUserDetailService.save(any(User.class)))
                .willReturn(user);

        mvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(user_ad_String))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("001200"));

    }
    @Test
    @WithMockUser("ROLE_USER")
    @DisplayName("로그인")
    public void login_test() throws Exception {

        String jsonString = "{\"userId\": \"001200\",\"name\": \"침착맨\"}";
        mvc.perform(post("/login")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
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