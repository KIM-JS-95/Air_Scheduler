package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.AirAPI.entity.json.Jsonschedules;
import org.AirAPI.repository.SchduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AWStextrack.class, Jsonschedules.class})
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application.properties")
class ScheduleServiceTest {
    @MockBean
    private ScheduleService scheduleService;
    @Mock
    private SchduleRepository schduleRepository;
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
        List<Schedule> mock_scheduleList = scheduleList;
        when(schduleRepository.saveAll(scheduleList))
                .thenReturn(mock_scheduleList);
        assertThat(mock_scheduleList.get(0).getCnt_from(), is("ICN"));
    }

    @Test
    public void finddata() {

        List<Schedule> mock_list = new ArrayList<>();
        // given
        Schedule schedule = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();

        mock_list.add(schedule);
        mock_list.add(schedule);
        mock_list.add(schedule);

        // 아이디로 검색하명 schedule 로 리턴할꺼야 ~~
        when(schduleRepository.findById(schedule.getId()))
                .thenReturn(mock_list);

        // when
        List<Schedule> threeDays_schedule = schduleRepository.findById(1);

        // then
        verify(schduleRepository, times(1)).findById(schedule.getId());
        assertThat(threeDays_schedule.get(0).getCnt_from(), is("BKK"));
    }
}