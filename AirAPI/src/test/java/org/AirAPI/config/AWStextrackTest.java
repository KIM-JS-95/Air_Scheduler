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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        Iterator<Block> blockIterator1 = AWStextrack.analyzeDoc(textractClient, fileInputStream);
        //AnalyzeDocumentResponse blockIterator2 = AWStextrack.analyzeDoc2(textractClient, fileInputStream);
        Map<String, List> map = ex1(blockIterator1);
        ex2(map.get("lines"), map.get("blocks"));
    }


    public Map<String, List> ex1(Iterator<Block> blockIterator) {
        List<Block> blocks = new ArrayList<>();
        List<Float> lines = new ArrayList<>();
        Map<String, List> map = new HashMap<>();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float top = block.geometry().boundingBox().top();
            if (block.blockType().equals(BlockType.WORD) && top < 0.05) {
                lines.add(block.geometry().polygon().get(0).x());
                blocks.add(block);
            }
        }
        map.put("lines", lines);
        map.put("blocks",blocks);
        return map;
    }

    public void ex2(List<Float> lines, List<Block> blocks) {
        String[] entity = {"", "", "", "", "", "", "", "", "", ""};
        String line = "";
        int line_change = 0;
        int size = blocks.size();
        for(int i=0; i<size; i++){
            blocks.get(i);
        }
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float left = block.geometry().boundingBox().left();
            float top = block.geometry().boundingBox().top();
            if (block.blockType().equals(BlockType.WORD) && top > 0.05 && left < 0.7) {
                int x = Math.round(block.geometry().polygon().get(1).x());
                for (int i = 1; i <= lines.size(); i++) {
                    if (x <= lines.get(i) && x >= lines.get(i - 1)) {
                        entity[i] = block.text();
                        LOGGER.info(block.text());
                    }
                }
            }
        }
    }
}
