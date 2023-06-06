package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.entity.json.Blocks;
import org.AirAPI.entity.json.Jsonschedules;
import org.AirAPI.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.Optional;

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
    private ScheduleRepository schduleRepository;
    @Autowired

    private Jsonschedules jsonschedules;
    private List<Blocks> blocks;

    /*
    @BeforeEach
    public void init() throws IOException {
        TextractClient textractClient = awstextrack.awsceesser();
        //String filePath = "C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        String filePath = "D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\static\\img\\sample.jpg";
        //FileInputStream fileInputStream = new FileInputStream(filePath);
        //blocks = awstextrack.analyzeDoc(textractClient, fileInputStream);
        mock_block = readJsonFile();
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
    public void findAll_data() {
        List<Schedule> mock_list = new ArrayList<>();
        // given
        Schedule schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        Schedule schedule2 = Schedule.builder()
                .id(2)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        Schedule schedule3 = Schedule.builder()
                .id(3)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();

        mock_list.add(schedule1);
        mock_list.add(schedule2);
        mock_list.add(schedule3);

        // 아이디로 검색하명 schedule 로 리턴할꺼야 ~~
        when(schduleRepository.getScheduleTreedays(2))
                .thenReturn(mock_list);

        // when
        List<Schedule> threeDays_schedule = schduleRepository.getScheduleTreedays(2);

        // then
        verify(schduleRepository, times(1)).getScheduleTreedays(2);
        assertThat(threeDays_schedule.get(0).getCnt_from(), is("BKK"));
    }

    @Test
    public void modify(){
        Schedule schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("LOS") // 도착
                .activity("OFF")
                .build();

        when(schduleRepository.findById(1))
                .thenReturn(schedule1);
        when(scheduleService.modify(1, schedule2))
                .thenReturn(true);
        boolean update = scheduleService.modify(1, schedule2);
        assertThat(update, is(true));
    }

    @Test
    public void delete(){
        Schedule schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cnt_from("BKK") // 출발
                .cnt_to("GMP") // 도착
                .activity("OFF")
                .build();
        when(schduleRepository.save(schedule1))
                .thenReturn(schedule1);
        when(schduleRepository.deleteById(1))
                .thenReturn(true);

        schduleRepository.deleteById(1);

        verify(schduleRepository, times(1)).deleteById(1);

    }
}