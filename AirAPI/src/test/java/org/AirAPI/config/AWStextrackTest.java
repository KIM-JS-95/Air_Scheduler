package org.AirAPI.config;

import org.AirAPI.entity.Schedule;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;

import java.io.*;
import java.math.BigDecimal;
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
    @Disabled
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
        //String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Iterator<Block> blocks = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        List<Block> blockslist = IteraterToList(blocks);
        Map<String, Float> scheduleIndex = getlines(blockslist);
        //List<Map.Entry<String, Float>> lines = sortByValue(map.get("lines"));
        ex2(scheduleIndex, blockslist);
    }

    public List<Block> IteraterToList(Iterator<Block> blockIterator) {
        List<Block> list = new ArrayList<>();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.blockType().equals(BlockType.WORD)) {
                list.add(block);
            }
        }
        return list;
    }

    public Map<String, Float> getlines(List<Block> blockslist) {
        Map<String, Float> lines = new HashMap<>();
        for (int i = 0; i < 1; i++) {
            Block block = blockslist.get(i);
            if (block.blockType().equals(BlockType.WORD)) {
                String mattcher = "(Date|Pairing|DC|C/1|C/O|Activity|From|STD|To|STA)";
                if (block.text().matches(mattcher)) {
                    lines.put(block.text(), block.geometry().polygon().get(0).x());
                }
            }
        }
        return lines;
    }
    public void ex2(Map<String, Float> scheduleIndex, List<Block> list) {
        Schedule schedule = new Schedule();
        for(int i=0; i<list.size(); i++){
            Block block = list.get(i);
            if (block.text().equals("Date")) {
                schedule.setDate(block.text());
            }
            if (block.text().equals("Pairing")) {
                schedule.setPairing(block.text());
            }
            if (block.text().equals("DC")) {
                schedule.setDc(block.text());
            }
            if (block.text().equals("C/1")) {
                schedule.setCi(block.text());
            }
            if (block.text().equals("C/O")) {
                schedule.setCo(block.text());
            }
            if (block.text().equals("Activity")) {
                schedule.setActivity(block.text());
            }
            if (block.text().equals("From")) {
                schedule.setCnt_from(block.text());
            }
            if (block.text().equals("STD")) {
                schedule.setStd(block.text());
            }
            if (block.text().equals("To")) {
                schedule.setCnt_to(block.text());
            }
            if (block.text().equals("STA")) {
                schedule.setSta(block.text());
            }
        }
    }

    public List<Map.Entry<String, Float>> sortByValue(Map<String, Float> map) {
        List<Map.Entry<String, Float>> entryList = new LinkedList<>(map.entrySet());
        entryList.sort(((o1, o2) -> {
            BigDecimal a = new BigDecimal(map.get(o1.getKey()));
            BigDecimal b = new BigDecimal(map.get(o2.getKey()));
            return a.compareTo(b);
        }));
        for (Map.Entry<String, Float> entry : entryList) {
            System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
        }
        return entryList;
    }

    @Test
    public void sorttest() {
        Map<String, Float> lines = new HashMap<>();
        lines.put("Blk", 0.916665f);
        lines.put("STA", 0.60274595f);
        lines.put("C/O", 0.27800912f);
        lines.put("STD", 0.48183724f);
        lines.put("Activity", 0.337937f);

        sortByValue(lines);
    }
}
