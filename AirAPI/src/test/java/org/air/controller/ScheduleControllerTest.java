package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.config.AWStextrack;
import org.air.config.HeaderSetter;
import org.air.config.SecurityConfig;
import org.air.entity.Schedule;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.repository.ScheduleRepository;
import org.air.service.CustomUserDetailService;
import org.air.service.ScheduleService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(ScheduleController.class)
@WebMvcTest(controllers = ScheduleController.class)
@ContextConfiguration(classes = {AWStextrack.class, SecurityConfig.class, HeaderSetter.class})
class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private String token;

    @BeforeEach
    public void init() {
        user = User.builder()
                .userid("001200")
                .name("tester")
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


}
