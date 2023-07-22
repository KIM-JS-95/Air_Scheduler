package org.air.config;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Configuration
public class PdfConverted {

    public void tearDown() {
        File[] tempFiles = new File(".").listFiles((dir, name) -> name.startsWith("page_") && name.endsWith(".jpg"));
        if (tempFiles != null) {
            for (File tempFile : tempFiles) {
                tempFile.delete();
            }
        }
    }
    public void testConvertPdfToJpg(File file) {
        try {
            PDDocument document = PDDocument.load(file);
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


}
