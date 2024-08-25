package org.air.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.air.entity.Schedule;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

//@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AWStextrackTest {
    private final Logger LOGGER = LoggerFactory.getLogger(AWStextrackTest.class);
    @Autowired
    public AWStextrack awStextrack;

    private static Region region;
    @Value("${accesskey}")
    private String accesskey;

    @Value("${secretkey}")
    private String secretkey;

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
            accesskey = prop.getProperty(accesskey);
            secretkey = prop.getProperty(secretkey);

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
        HashMap<String, String> map = new HashMap<>();
        blocks.forEach(callback -> {
            if (callback.blockType().equals("WORD")) {
                map.put(callback.id(), callback.text());
            }
        });

        ex2(map, blocks);
    }


    public static boolean isDateValid(String dateString) {
        try {
            String dateFormatPattern = "^\\d{2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\d{2}$";
            return Pattern.matches(dateFormatPattern, dateString);
        } catch (Exception e) {
            return false;
        }
    }

    public void ex2(HashMap<String, String> map, List<Block> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        String[] weeks = {"null"};
        try {
            for (int i = 0; i < list.size(); i++) {
                Block block = list.get(i);
                int index = block.columnIndex();
                String[] ids = block.relationships().isEmpty() ? weeks : block.relationships().get(0).ids().toArray(new String[0]);
                if (list.get(i).rowIndex() == 1) continue;
                if (index == 13) {
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
                if (block.relationships() != null) {
                    if (index == 1) {
                        if (ids.length == 1) {
                            if (isDateValid(map.get(ids[0]))) {
                                schedule.setDate(map.get(ids[0]));
                            } else {
                                schedule.setPairing(map.get(ids[0]));
                            }
                        } else {
                            schedule.setDate(map.get(ids[0]));
                            schedule.setPairing(map.get(ids[1]));
                        }
                    } else if (index == 2) {
                        schedule.setDc(map.get(ids[0]));
                    } else if (index == 3) {
                        schedule.setCi(map.get(ids[0]));
                    } else if (index == 4) {
                        schedule.setCo(map.get(ids[0]));
                    } else if (index == 5) {
                        schedule.setActivity(map.get(ids[0]));
                    } else if (index == 6) {
                        schedule.setCntfrom(map.get(ids[0]));
                    } else if (index == 7) {
                        schedule.setStdl(map.get(ids[0]));
                    } else if (index == 8) {
                        schedule.setStdb(map.get(ids[0]));
                    } else if (index == 9) {

                        schedule.setCntto(map.get(ids[0]));
                    } else if (index == 10) {
                        schedule.setStal(map.get(ids[0]));
                    } else if (index == 11) {
                        schedule.setStab(map.get(ids[0]));
                    } else if (index == 12) {
                        String hotel = "";
                        for (int j = 0; j < ids.length; j++) {
                            hotel += map.get(ids[j]);
                        }
                        schedule.setAchotel(hotel);
                    } else if (index == 13) {
                        schedule.setBlk(map.get(ids[0]));
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        schedules.forEach((n) -> System.out.println(n));
    }

    @Test
    public void readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("C:\\Users\\KIMJAESUNG\\IdeaProjects\\Air_Scheduler_pack\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");

        try {
            List<Block> entities = objectMapper.readValue(file, new TypeReference<List<Block>>() {
            });
            System.out.println(entities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
