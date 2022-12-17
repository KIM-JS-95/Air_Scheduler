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
    private ScheduleService scheduleService;
    @Mock
    private SchduleRepository schduleRepository;
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

    @DisplayName("스케쥴 저장 테스트")
    @Test
    public void save() {
        lenient()
                .when(schduleRepository.saveAll(scheduleList))
                .thenReturn(scheduleList);

        assertThat(scheduleList.get(0).getCnt_from(), Matchers.is("BKK"));
    }

    @DisplayName("3개의 데이터를 호출")
    @Test
    public void finddata() {
        // call findById(1)
        lenient()
                .when(scheduleService.findData(1)).thenReturn(schedule1);
        assertThat(schedule1.getCnt_to(), Matchers.is("GMP"));

    }
}