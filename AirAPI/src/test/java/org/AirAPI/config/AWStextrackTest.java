package org.AirAPI.config;

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
        Map<String, Float> map = ex1(blockIterator1);
        ex2(map, blockIterator1);
    }


    public Map<String, Float> ex1(Iterator<Block> blockIterator) {
        List<Block> blocks = new ArrayList<>();
        Map<String, Float> lines = new HashMap<>();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float top = block.geometry().boundingBox().top();
            if (block.blockType().equals(BlockType.WORD) && top < 0.05) {
                String mattcher = "(Date|Pairing|DC|C/I|C/O|Activity|From|STD|To|STA|AC|Blk)";
                if (block.text().matches(mattcher)) {
                    LOGGER.info(block.text());
                    lines.put(block.text(), block.geometry().polygon().get(0).x());
                }
            }
        }
        return lines;
    }

    public void ex2(Map<String, Float> lines, Iterator<Block> blockIterator) {
        String[] entity = {"", "", "", "", "", "", "", "", "", ""};
        String line = "";
        int line_change = 0;
        sortByValue(lines);
        for (Map.Entry entry : lines.entrySet()) {
            System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }
        /*
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            float top = block.geometry().boundingBox().top();
            if (block.blockType().equals(BlockType.WORD) && top > 0.05) {
                if (block.geometry().polygon().get(0).x())

            }
        }
        */
    }

    public void sortByValue(Map<String, Float> map) {
        List<Map.Entry<String, Float>> entryList = new LinkedList<>(map.entrySet());
        entryList.sort(((o1, o2) -> {
            BigDecimal a = new BigDecimal(map.get(o1.getKey()));
            BigDecimal b = new BigDecimal(map.get(o2.getKey()));
            return a.subtract(b).floatValue();
        });
        for (Map.Entry<String, Float> entry : entryList) {
            System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
        }
    }
}
