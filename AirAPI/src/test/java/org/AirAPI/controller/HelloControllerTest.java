package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.AirAPI.config.AWStextrack;
import org.AirAPI.repository.SchduleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AWStextrack awstextrack;

    @MockBean
    private SchduleRepository schduleRepository;

    private Schedule schedule1;
    List<Schedule> scheduleList = new ArrayList<>();

    @BeforeEach
    public void init() {
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

    @DisplayName("기본 테스트")
    @Test
    public void hello_test() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    @DisplayName("save_Test")
    public void save_test() throws Exception{

        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleList)))
                .andExpect(status().isOk())
                .andExpect(header().string("message","성공 코드"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].std").value("0000"))
                .andDo(print());
    }


    @Test
    @DisplayName("textrack_test")
    public void jpg_save_test() throws Exception {

        // given
        final String fileName = "sample"; //파일명
        final String contentType = "jpg"; //파일타입

        ClassPathResource resource = new ClassPathResource("static/img/sample.jpg");

        final String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg"; //파일경로
        //final String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile img = new MockMultipartFile(
                "file",  "sample.jpg" ,contentType, fileInputStream);

        // when
        mvc.perform(multipart("/jpg")
                .file(img)
        ).andExpect(status().isOk());

    }

}