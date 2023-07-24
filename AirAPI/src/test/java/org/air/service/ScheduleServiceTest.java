package org.air.service;

import org.air.config.AWStextrack;
import org.air.entity.Schedule;
import org.air.entity.json.Blocks;
import org.air.entity.json.Jsonschedules;
import org.air.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.regions.servicemetadata.StsServiceMetadata;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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

        /*
        List<Schedule> scheduleList = jsonschedules.getschedules(map, block);
        List<Schedule> mock_scheduleList = scheduleList;
        when(schduleRepository.saveAll(scheduleList))
                .thenReturn(mock_scheduleList);
        assertThat(mock_scheduleList.get(0).getCnt_from(), is("ICN"));
    */
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
                    schedule.setCnt_from(map.get(ids[0]));
                } else if (index == 7) {
                    schedule.setStd(map.get(ids[0]));
                } else if (index == 8) {
                    schedule.setCnt_to(map.get(ids[0]));
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
    public void getSchedules_ThreeDay() {
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
        when(schduleRepository.findByDateBetween("2023-05-05", "2023-05-08"))
                .thenReturn(mock_list);

        // when
        List<Schedule> threeDays_schedule = schduleRepository
                .findByDateBetween("2023-05-05", "2023-05-08");

        // then
        verify(schduleRepository, times(1))
                .findByDateBetween("2023-05-05", "2023-05-08");
        assertThat(threeDays_schedule.get(0).getCnt_from(), is("BKK"));
    }

    @Test
    public void modify() {
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
    public void delete() {
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