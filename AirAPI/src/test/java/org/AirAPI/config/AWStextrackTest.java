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
        String date = null;
        String pairing = null;
        String dc = null;
        String ci = null;
        String co = null;
        String activity = null;
        String cnt_from = null;
        String std = null;
        String cnt_to = null;
        String sta = null;
        String comment = null;

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float left = block.geometry().boundingBox().left();
            float top = block.geometry().boundingBox().top();
            if (block.blockType().equals(BlockType.WORD) && top > 0.05 && left < 0.7) {
                if (left <= 0.1) {
                    date = block.text();
                } else if (0.1 < left && left <= 0.18) {
                    pairing = block.text();
                } else if (0.18 < left && left <= 0.24) {
                    dc = block.text();
                } else if (0.24 < left && left <= 0.3) {
                    ci = block.text();
                } else if (0.3 < left && left <= 0.37) {
                    co = block.text();
                } else if (0.37 < left && left <= 0.46) {
                    activity = block.text();
                } else if (0.46 < left && left <= 0.52) {
                    cnt_from = block.text();
                } else if (0.52 < left && left <= 0.58) {
                    std = block.text();
                } else if (0.58 < left && left <= 0.65) {
                    cnt_to = block.text();
                } else if (0.65 < left && left <= 0.7) {
                    sta = block.text();
                } else {
                    comment = block.text();
                }
                Schedule schedule = Schedule.builder()
                        .date(date)
                        .pairing(pairing)
                        .dc(dc)
                        .ci(ci)
                        .co(co)
                        .activity(activity)
                        .cnt_from(cnt_from)
                        .std(std)
                        .cnt_to(cnt_to)
                        .sta(sta)
                        .comment(comment)
                        .build();
                schedules.add(schedule);
            }
        }
        try {
            System.out.println(schedules.get(0));
        } catch (Exception e) {
            System.out.println("error! : " + e);
        }
    }
}
