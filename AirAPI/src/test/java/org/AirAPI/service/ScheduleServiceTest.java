package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.AirAPI.entity.json.Jsonschedules;
import org.AirAPI.repository.SchduleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AWStextrack.class, Jsonschedules.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application.properties")
class ScheduleServiceTest {
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private SchduleRepository schduleRepository;
    @Autowired
    public AWStextrack awstextrack;
    @Autowired
    private Jsonschedules jsonschedules;
    private List<Blocks> blocks;


    /*
    @BeforeEach
    public void init() throws FileNotFoundException {
        TextractClient textractClient = awstextrack.awsceesser();
        String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        blocks = awstextrack.analyzeDoc(textractClient, fileInputStream);
    }
*/
    @Test
    @DisplayName("save test")
    public void save() throws IOException, ParseException {

        HashMap<String, String> map = new HashMap<>();
        blocks = jsonschedules.readJsonFile();
        blocks.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            }
        });
        List<Schedule> scheduleList = jsonschedules.getschedules(map, blocks);

        lenient()
                .when(schduleRepository.saveAll(scheduleList))
                .thenReturn(scheduleList);

        assertThat(scheduleList.get(0).getCnt_from(), Matchers.is("ICN"));

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