package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.config.AWStextrack;
import org.AirAPI.config.SecurityConfig;
import org.AirAPI.entity.Authority;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.User;
import org.AirAPI.jwt.JwtAuthenticationFilter;
import org.AirAPI.jwt.JwtTokenProvider;
import org.AirAPI.repository.SchduleRepository;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.AirAPI.service.CustomUserDetailService;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AWStextrack awstextrack;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private SchduleRepository schduleRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ScheduleService scheduleService;

    private Schedule schedule1;
    List<Schedule> scheduleList = new ArrayList<>();

    @BeforeEach
    public void init() throws Exception {
        schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        scheduleList.add(schedule1);

    }

    @Test
    @DisplayName("메인 접속 테스트")
    public void 메인페이지에접속합니다() throws Exception {
        Set<Authority> authorities = Set.of(Authority.USER);

        String token = jwtTokenProvider.createToken("001200", "침착맨");
        User userDetails = User.builder()
                .userId("001200")
                .name("침착맨")
                .authorities(authorities)
                .build();

        when(customUserDetailService
                .loadUserByUsername(jwtTokenProvider.getUserPk(token)))
                .thenReturn(userDetails);

        mvc.perform(get("/home")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }



    @DisplayName("기본 테스트")
    @Test
    public void No_refresh_Token() throws Exception {
        Set<Authority> authorities = Set.of(Authority.USER);

        String token = jwtTokenProvider.createToken("001200", "침착맨");
        User userDetails = User.builder()
                .userId("001200")
                .name("침착맨")
                .authorities(authorities)
                .build();

        when(customUserDetailService
                .loadUserByUsername(jwtTokenProvider.getUserPk(token)))
                .thenReturn(userDetails);
        try {
            mvc.perform(get("/home").header("Authorization", token))
                    .andExpect(status().isOk())
                    .andDo(print());
        } catch(IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("리프레시 토큰이 없습니다.");
        }
    }


    @Test
    @DisplayName("textrack_test")
    public void jpg_save_test() throws Exception {

        Set<Authority> authorities = Set.of(Authority.USER);

        String token = jwtTokenProvider.createToken("001200", "침착맨");
        User userDetails = User.builder()
                .userId("001200")
                .name("침착맨")
                .authorities(authorities)
                .build();

        when(customUserDetailService
                .loadUserByUsername(jwtTokenProvider.getUserPk(token)))
                .thenReturn(userDetails);

        final String contentType = "jpg"; //파일타입
        final String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg"; //파일경로
        //final String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile img = new MockMultipartFile(
                "file",  "sample.jpg" ,contentType, fileInputStream);

        mvc.perform(multipart("/upload")
                        .file(img)
                        .header("Authorization",token)
                ).andExpect(status().isOk())
                .andDo(print());
    }

}