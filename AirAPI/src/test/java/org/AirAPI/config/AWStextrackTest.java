package org.AirAPI.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.util.*;

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
    //@Disabled
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
        String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        List<Block> blocks = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        ex2(blocks);
    }


    public void ex2(List<Block> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
/*
* 타입 CELL에서 id값을 획득해서 word에서 검색하는 방식으로 해야함
* id : word
*
* */
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).blockType().toString()=="CELL" && list.get(i).rowIndex() != 1) {
                Block block = list.get(i);
                int index = block.columnIndex();
                String chileText = block.text();
                if (index == 1) {
                    schedule.setDate(chileText);
                } else if (index == 2) {
                    schedule.setPairing(chileText);
                } else if (index == 3) {
                    schedule.setDc(chileText);
                } else if (index == 4) {
                    schedule.setCi(chileText);
                } else if (index == 5) {
                    String[] units = chileText.split(" ");
                    if (units.length == 1) {
                        schedule.setActivity(units[0]);
                    } else {
                        schedule.setCo(units[0]);
                        schedule.setActivity(units[1]);
                    }
                } else if (index == 6) {
                    schedule.setCnt_from(chileText);
                } else if (index == 7) {
                    schedule.setStd(chileText);
                } else if (index == 8) {
                    schedule.setCnt_to(chileText);
                } else if (index == 9) {
                    schedule.setSta(chileText);
                } else if (index == 10) {
                    schedule.setAchotel(chileText);
                } else if (index == 11) {
                    schedule.setBlk(chileText);
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
            }
        }
        schedules.forEach((n) -> System.out.println(n));
    }


    public List<Block> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //File file = new File("C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
        File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");

        try {
            List<Block> entities = objectMapper.readValue(file, new TypeReference<List<Block>>() {
            });
            //return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
