package org.AirAPI.Service;

import org.AirAPI.Entity.Schedule;
import org.AirAPI.Repository.SchduleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    ScheduleService scheduleService;
    @Mock
    SchduleRepository schduleRepository;
    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule schedule3;
    List<Schedule> scheduleList = new ArrayList<>();

    @BeforeEach
    public void init() {

        schedule1 = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .from("GMP") // 출발
                .to("GMP") // 도착
                .activity("OFF")
                .build();

        schedule2 = Schedule.builder()
                .id(2)
                .date("01Nov22")
                .std("1000") // 출발 시간
                .sta("2359") // 도착 시간
                .from("GMP") // 출발
                .to("JPN") // 도착
                .activity("OFF")
                .build();

        schedule3 = Schedule.builder()
                .id(3)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .from("GMP") // 출발
                .to("BKK") // 도착
                .activity("OFF")
                .build();

        scheduleList.add(schedule1);
        scheduleList.add(schedule2);
        scheduleList.add(schedule3);
    }

    @DisplayName("스케쥴 저장 테스트")
    @Test
    public void save(){
        lenient().when(schduleRepository.saveAll(scheduleList)).thenReturn(scheduleList);
        assertThat(scheduleList.get(2).getTo(), Matchers.is("BKK"));
    }
    @DisplayName("3개의 데이터를 호출")
    @Test
    public void finddata() {
        // call findById(1)
        lenient().when(scheduleService.findData(1)).thenReturn(schedule1);
        assertThat(schedule1.getFrom(), Matchers.is("GMP"));

    }
}