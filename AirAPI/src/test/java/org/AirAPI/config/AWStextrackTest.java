package org.AirAPI.config;

import org.AirAPI.entity.Schedule;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AWStextrackTest {
    private final Logger LOGGER = LoggerFactory.getLogger(AWStextrackTest.class);
    @Autowired
    public AWStextrack awStextrack;

    private static Region region;
    private static String accesskey = "";
    private static String secretkey = "";
    private static TextractClient textractClient;

    @BeforeAll
    public void access_init() {
        region = Region.US_WEST_2;

        try (InputStream input = AWStextrackTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            prop.load(input);
            accesskey = prop.getProperty("accesskey");
            secretkey = prop.getProperty("secretkey");

            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accesskey, secretkey);

            textractClient = TextractClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(region)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void docTest() throws IOException {
        String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Iterator<Block> blockIterator = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        List<Schedule> schedules = new ArrayList<>();
        String line = "";
        int line_change = 0;

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float left = block.geometry().boundingBox().left();
            float top = block.geometry().boundingBox().top();
            if (line_change != Math.round(top * 100)) {
                String[] s = line.split("- ");
                // 문자열을 쪼갠 배열을 엔티티에 저장해야함
                line = "";
            }
            if (block.blockType().equals(BlockType.WORD) && top > 0.05 && left < 0.7) {
                line_change = Math.round(top * 100);
                line += block.text() + "- ";
            }
        }
        try {
            System.out.println(schedules.get(0));
        } catch (Exception e) {
            System.out.println("error! : " + e);
        }
    }
}
