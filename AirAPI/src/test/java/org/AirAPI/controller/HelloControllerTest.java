package org.AirAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.Entity.Schedule;
import org.AirAPI.config.AWStextrack;
import org.AirAPI.config.AmazonTextractServiceIntegrationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = HelloController.class)
class HelloControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AWStextrack awstextrack;

    private Schedule schedule1;
    List<Schedule> scheduleList = new ArrayList<>();

    @BeforeEach
    public void init() {
        schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        scheduleList.add(schedule1);
    }

    @DisplayName("기본 테스트")
    @Test
    public void hello_test() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    @DisplayName("save_Test")
    public void save_test() throws Exception{

        Schedule schedule = new Schedule();
        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleList)))
                .andExpect(status().isOk())
                .andExpect(header().string("message","성공 코드"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].std").value("0000"))
                .andDo(print());
    }

    @Test
    @DisplayName("display_schedules")
    public void schedule() throws Exception {
        mvc.perform(get("/schedule"))
                .andExpect(status().isOk());
    }

    private static TextractClient textractClient;
    private static Region region;
    private static String sourceDoc = "";
    private static String bucketName = "";
    private static String docName = "";

    @BeforeAll
    public static void setUp() throws IOException {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "AKIA3JVA2CBMGVTJWVNT",
                " 1ABwHai1d9ZfUKRcF+TTyWuqavbBk8AVcgiSncQp"
        );

        textractClient = AWStextrack.

        textractClient = TextractClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        try (InputStream input = AmazonTextractServiceIntegrationTest.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            //load a properties file from class path, inside static method
            prop.load(input);
            // Populate the data members required for all tests
            sourceDoc = prop.getProperty("sourceDoc");
            bucketName = prop.getProperty("bucketName");
            docName = prop.getProperty("docName");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @DisplayName("textrack test")
    public void textrack() {
        awstextrack.analyzeDoc(textractClient, sourceDoc);
    }

}