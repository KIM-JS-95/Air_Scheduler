package org.air.controller;

import org.air.config.HeaderSetter;
import org.air.config.SecurityConfig;
import org.air.entity.Schedule;
import org.air.repository.ScheduleRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(ScheduleController.class)
@WebMvcTest(controllers = ScheduleController.class)
@ContextConfiguration(classes = {SecurityConfig.class, HeaderSetter.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;
    private File fileInputStream;


    public void setUp() throws IOException {
        String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\pdf_sample.pdf";
        fileInputStream = new File(filePath);
    }

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
            String  dir = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\converted\\1.jpg";
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

}