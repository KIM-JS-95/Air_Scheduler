package org.air.controller;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class ScheduleControllerTest {

    private MockMultipartFile pdfFile;

    @BeforeEach
    public void setUp() throws IOException {

        String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        File fileInputStream = new File(filePath);
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
    public void testConvertPdfToJpg() {

        try {
            File convertedFile = new File("converted.jpg");
            File jpgFile = new File("test.jpg");
            PDDocument document = PDDocument.load(convertedFile);

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0); // 첫 번째 페이지를 이미지로 변환
            // 이미지를 JPG 파일로 저장
            ImageIO.write(image, "jpg", jpgFile);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}