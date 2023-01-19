package org.AirAPI.service;

import org.AirAPI.entity.Schedule;
import org.AirAPI.repository.SchduleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ScheduleServiceTest {
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
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

    @Test
    @DisplayName("스케쥴 저장 테스트")
    public void save() {
        lenient()
                .when(schduleRepository.saveAll(scheduleList))
                .thenReturn(scheduleList);

        assertThat(scheduleList.get(0).getCnt_from(), Matchers.is("BKK"));
    }

    @Test
    @DisplayName("3개의 데이터를 호출")
    public void finddata() {

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
        given(scheduleService.findData(1)).willReturn(schedule1);

        assertThat(schedule1.getCnt_to(), Matchers.is("GMP"));
    }


}