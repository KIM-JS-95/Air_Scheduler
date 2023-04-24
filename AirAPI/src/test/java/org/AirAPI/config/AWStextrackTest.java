package org.AirAPI.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        //String filePath = "C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Iterator<Block> blocks = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        List<Block> blockslist = IteraterToList(blocks);
        Map<String, Float> scheduleIndex = getlines(blockslist);
        List<Float> lines = sortByValue(scheduleIndex);
        //System.out.println("SIze: "+lines.toString());
        ex2(lines, blockslist);
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
        for (int i = 0; i < blockslist.size(); i++) {
            Block block = blockslist.get(i);
            String mattcher = "(Date|Pairing|DC|C/1|C/O|Activity|From|STD|To|STA|AC/Hotel)";
            if (block.text().matches(mattcher)) {
                lines.put(block.text(), block.geometry().polygon().get(0).x());
            }
            if (block.text() == "AC/Hotel") break;
        }
        return lines;
    }

    public void ex2(List<Float> scheduleIndex, List<Block> list) {
        Schedule schedule = new Schedule();
        String date = "";
        String pairing ="";
        String dc = "";
        String ci = "";
        String co = "";
        String activity = "";
        String cnt_from = "";
        String std = "";
        String cnt_to = "";
        String sta = "";
        Float y1=0f;
        for (int i=1; i<list.size(); i++) {
            String text = list.get(i).text();
            Float x = list.get(i).geometry().polygon().get(0).x();
            Float y2 = list.get(i).geometry().polygon().get(0).y();

            if (0f <x && x < scheduleIndex.get(1)) {
                date=text;
            } else if (scheduleIndex.get(1) <x && x < scheduleIndex.get(2)) {
                pairing=text;
            } else if (scheduleIndex.get(2) <x && x < scheduleIndex.get(3)) {
                dc=text;
            } else if (scheduleIndex.get(3) <x &&x < scheduleIndex.get(4)) {
                ci=text;
            } else if (scheduleIndex.get(4) <x &&x < scheduleIndex.get(5)) {
                co=text;
            } else if (scheduleIndex.get(5) <x &&x < scheduleIndex.get(6)) {
                activity=text;
            } else if (scheduleIndex.get(6) <x &&x < scheduleIndex.get(7)) {
                cnt_from=text;
            } else if (scheduleIndex.get(7) <x &&x < scheduleIndex.get(8)) {
                std=text;
            } else if (scheduleIndex.get(8) <x &&x < scheduleIndex.get(9)) {
                cnt_to=text;
            } else if (scheduleIndex.get(9) <x &&x < scheduleIndex.get(10)) {
                sta=text;
            }
            if(y2-y1 > 0.01f){
                y1= y2;
                schedule.setDate(date);
                schedule.setPairing(pairing);
                schedule.setDc(dc);
                schedule.setCi(ci);
                schedule.setCo(co);
                schedule.setActivity(activity);
                schedule.setCnt_from(cnt_from);
                schedule.setStd(std);
                schedule.setCnt_to(cnt_to);
                schedule.setSta(sta);
                System.out.println(schedule.toString());
            }
        }
    }

    public List<Float> sortByValue(Map<String, Float> map) {
        List<Float> entryList = new LinkedList<>(map.values());
        entryList.sort(Float::compareTo);
        return entryList;
    }

    @Test
    public void setEntity() throws IOException {
        List<Float> scheduleIndex = new ArrayList<>();
        scheduleIndex.add(0.004148122f);
        scheduleIndex.add(0.08701322f);
        scheduleIndex.add(0.17003645f);
        scheduleIndex.add(0.21753336f);
        scheduleIndex.add(0.27800912f);
        scheduleIndex.add(0.337937f);
        scheduleIndex.add(0.4218124f);
        scheduleIndex.add(0.48183724f);
        scheduleIndex.add(0.5422942f);
        scheduleIndex.add(0.60274595f);
        scheduleIndex.add(0.6360592246055603f);

        List<Dummy> block = readJsonFile();
        ex2_test(scheduleIndex, block);
    }


    public void ex2_test(List<Float> scheduleIndex, List<Dummy> list) {

        Schedule schedule = new Schedule();
        try {
            for (Dummy block : list) {
                if (Float.parseFloat(block.getX()) < scheduleIndex.get(1)) {
                    schedule.setDate(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(2)) {
                    schedule.setPairing(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(3)) {
                    schedule.setDc(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(4)) {
                    schedule.setCi(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(5)) {
                    schedule.setCo(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(6)) {
                    schedule.setActivity(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(7)) {
                    schedule.setCnt_from(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(8)) {
                    schedule.setStd(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(9)) {
                    schedule.setCnt_to(block.getText());
                } else if (Float.parseFloat(block.getX()) < scheduleIndex.get(10)) {
                    schedule.setSta(block.getText());
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        System.out.println(schedule.toString());
    }

    public List<Dummy> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //File file = new File("C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
        File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");

        try {
            List<Dummy> entities = objectMapper.readValue(file, new TypeReference<List<Dummy>>() {
            });
            return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
