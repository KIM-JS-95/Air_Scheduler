package org.air.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.config.AWStextrack;
import org.air.config.HeaderSetter;
import org.air.config.SecurityConfig;
import org.air.entity.Schedule;
import org.air.jwt.JwtTokenProvider;
import org.air.repository.ScheduleRepository;
import org.air.service.ScheduleService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(ScheduleController.class)
@WebMvcTest(controllers = ScheduleController.class)
@ContextConfiguration(classes = {AWStextrack.class,SecurityConfig.class, HeaderSetter.class})
class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleRepository scheduleRepository;

    private File fileInputStream;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;



    @BeforeEach
    public void setUp(@Autowired ScheduleService scheduleService) throws IOException {
        String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\pdf_sample.pdf";
        fileInputStream = new File(filePath);
    }

    /*
    @AfterEach
    public void tearDown() {
        // Clean up temporary files after each test
        File[] tempFiles = new File(".").listFiles((dir, name) -> name.startsWith("page_") && name.endsWith(".jpg"));
        if (tempFiles != null) {
            for (File tempFile : tempFiles) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testConvertPdfToJpg() throws IOException {

        setUp();
        try {
            PDDocument document = PDDocument.load(fileInputStream);
            String dir = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\converted\\1.jpg";
            File jpgFile = new File(dir);

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0);
            // 이미지를 JPG 파일로 저장
            ImageIO.write(image, "jpg", jpgFile);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    @Test
    public void save() throws Exception {
        // Create a sample file to upload
        MockMultipartFile file =
                new MockMultipartFile("file", "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\November.jpg", MediaType.IMAGE_JPEG_VALUE, "Test file content".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // You can add additional assertions based on your controller's behavior
    }

}
