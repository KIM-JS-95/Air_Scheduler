package org.AirAPI.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.AirAPI.repository.SchduleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AWStextrack.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application.properties")
class ScheduleServiceTest {
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private SchduleRepository schduleRepository;
    @Autowired
    public AWStextrack awstextrack;
    private List<Block> blocks;
    private List<Blocks> mock_block = new ArrayList<Blocks>();
    @BeforeEach
    public void init() throws IOException {
        TextractClient textractClient = awstextrack.awsceesser();
        //String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //FileInputStream fileInputStream = new FileInputStream(filePath);
        //blocks = awstextrack.analyzeDoc(textractClient, fileInputStream);
        mock_block = readJsonFile();
    }

    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        //File file = new File("C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        try {
            List<Blocks> entities = objectMapper.readValue(file, new TypeReference<List<Blocks>>() {
            });
            return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    @DisplayName("save test")
    public void save() throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        mock_block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            }
        });

        List<Schedule> scheduleList = awstextrack.texttoEntity_test(map, mock_block);
        verify(scheduleList,atLeastOnce());
        lenient()
                .when(schduleRepository.saveAll(scheduleList))
                .thenReturn(scheduleList);

        //assertThat(scheduleList.get(0).getCnt_from(), Matchers.is("BKK"));

    }

    @Test
    @DisplayName("3개의 데이터를 호출")
    public void finddata() {
        // when
        Schedule schedule = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        when(schduleRepository.findById(any())).thenReturn(Optional.ofNullable(schedule));


        // given
        Schedule schedule2 = scheduleService.findData(1);
        verify(scheduleService, times(1)).findData(1);
        assertThat(schedule2.getCnt_to(), Matchers.is("GMP"));
    }


}