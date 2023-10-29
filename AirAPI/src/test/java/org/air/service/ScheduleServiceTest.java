package org.air.service;

import org.air.config.AWStextrack;
import org.air.entity.Schedule;
import org.air.entity.json.Blocks;
import org.air.entity.json.Jsonschedules;
import org.air.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AWStextrack.class, Jsonschedules.class, ScheduleService.class})
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application.properties")
class ScheduleServiceTest {
    @MockBean
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private Jsonschedules jsonschedules;

    @Autowired
    private AWStextrack awstextrack;
    private List<Blocks> block;

    @Test
    @DisplayName("save test")
    public void save() throws IOException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        List<Blocks> list_block = new ArrayList<>();
        block = jsonschedules.readJsonFile();
        block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            } else if (callback.getBlockType().toString().equals("CELL")) {
                list_block.add(callback);
            }
        });

        List<Schedule> schedules_test = texttoEntity_test(map, list_block);

        schedules_test.forEach(callback -> {
            System.out.println("date : " + callback.toString());
        });

    }

    public List<Schedule> texttoEntity_test(HashMap<String, String> map, List<Blocks> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();

        for (int i = 0; i < list.size(); i++) {
            Blocks block = list.get(i);
            int index = block.getColumnIndex();
            if (list.get(i).getRowIndex() == 1 || index == 2) continue;
            if (index == 11) {
                schedules.add(schedule);
                schedule = new Schedule();
            }
            if (block.getRelationships() != null) {
                String[] ids = block.getRelationships()[0].getIds();
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
                } else if (index == 3) {
                    schedule.setDc(map.get(ids[0]));
                } else if (index == 4) {
                    schedule.setCi(map.get(ids[0]));
                } else if (index == 5) {
                    schedule.setActivity(map.get(ids[0]));
                } else if (index == 6) {
                    schedule.setCntFrom(map.get(ids[0]));
                } else if (index == 7) {
                    schedule.setStd(map.get(ids[0]));
                } else if (index == 8) {
                    schedule.setCntTo(map.get(ids[0]));
                } else if (index == 9) {
                    schedule.setSta(map.get(ids[0]));
                } else if (index == 10) {
                    String hotel = "";
                    for (int j = 0; j < ids.length; j++) {
                        hotel += map.get(ids[j]);
                    }
                    schedule.setAchotel(hotel);
                } else if (index == 11) {
                    schedule.setBlk(map.get(ids[0]));
                }
            }
        }
        return schedules;
    }

    public static boolean isDateValid(String dateString) {
        try {
            String dateFormatPattern = "^\\d{2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\d{2}$";
            return Pattern.matches(dateFormatPattern, dateString);
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void getSchedules_ThreeDay() throws ParseException {
        List<Schedule> mock_list = new ArrayList<>();
        // given
        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("BKK") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();
        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("02Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("BKK") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();
        Schedule schedule3 = Schedule.builder()
                .id(3L)
                .date("03Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("BKK") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        mock_list.add(schedule1);
        mock_list.add(schedule2);
        mock_list.add(schedule3);

        String sdate = "01Nov22";
        String edate = "03Nov22";

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy");

        // 아이디로 검색하명 schedule 로 리턴할꺼야 ~~
        when(scheduleRepository.findByDateBetween(sdate, edate))
                .thenReturn(mock_list);

        // when
        List<Schedule> threeDays_schedule = scheduleRepository
                .findByDateBetween(sdate, edate);

        // then
        verify(scheduleRepository, times(1))
                .findByDateBetween(sdate, edate);
        assertThat(threeDays_schedule.get(0).getCntFrom(), is("BKK"));
    }

    @Test
    public void modify() {
        Optional<Schedule> origin_schdule = Optional.ofNullable(Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("BKK") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build());

        // 업데이트를 원하는 대상
        Schedule update_schedule = Schedule.builder()
                .id(1L)
                .date("01Nov23")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("BKK") // 출발
                .cntTo("LOS") // 도착
                .activity("OFF")
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(origin_schdule);
        Schedule update = scheduleService.modify(0L,update_schedule);
        assertThat(update.getDate(), is("01Nov23"));

    }

    @Test
    public void delete() {

        scheduleRepository.truncateTable();

    }
}