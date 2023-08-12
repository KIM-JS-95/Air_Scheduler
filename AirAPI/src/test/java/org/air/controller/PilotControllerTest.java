package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.config.AWStextrack;
import org.air.entity.Schedule;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.repository.ScheduleRepository;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.air.service.CustomUserDetailService;
import org.air.service.ScheduleService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ExtendWith(SpringExtension.class)
@Import(PilotController.class)
@WebMvcTest(controllers = PilotController.class)
@ContextConfiguration(classes = {AWStextrack.class, JwtTokenProvider.class})
//@EnableWebSecurity
public class PilotControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AWStextrack awstextrack;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private CustomUserDetailService customUserDetailService;
    @MockBean
    private ScheduleRepository schduleRepository;
    @MockBean
    private TokenRepository tokenRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ScheduleService scheduleService;
    private Schedule schedule1;
    List<Schedule> scheduleList = new ArrayList<>();
    User user = null;
    List<Schedule> l = new ArrayList<>();
    @BeforeEach
    public void init() {
        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        l.add(schedule1);

    }


    @Test
    @DisplayName("main_page_access")
    @WithMockUser
    public void 메인페이지에접속합니다() throws Exception {
        String token = jwtTokenProvider.createToken("001200", "침착맨");

        mvc.perform(get("/home")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("Token_test")
    @Test
    @WithMockUser
    public void Token_test() throws Exception {
        String token = jwtTokenProvider.createToken("001200", "침착맨");
        User userDetails = User.builder()
                .userid("001200")
                .name("침착맨")
                .build();

        try {
            mvc.perform(get("/home").header("Authorization", token))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", token))
                    .andDo(print());
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("리프레시 토큰이 없습니다.");
        }

    }

    @Test
    @Disabled
    @DisplayName("textrack_test")
    public void jpg_save_test() throws Exception {
        String token = jwtTokenProvider.createToken("001200", "침착맨");
        when(customUserDetailService.loadUserById("001200"))
                .thenReturn(user);

        final String contentType = "jpg"; //파일타입
        final String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg"; //파일경로
        //final String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile img = new MockMultipartFile(
                "file", "sample.jpg", contentType, fileInputStream);

        //when(scheduleService.save(img.getInputStream())).thenReturn(true);
        mvc.perform(multipart("/upload")
                        .file(img)
                        .header("Authorization", token)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getSchedules() throws Exception {

        when(scheduleService.getSchedules(any(), any())).thenReturn(l);
        JSONObject obj = new JSONObject();
        String answer = obj.put("schedule",l).toString();

        mvc.perform(get("/getschedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(answer));
    }

}