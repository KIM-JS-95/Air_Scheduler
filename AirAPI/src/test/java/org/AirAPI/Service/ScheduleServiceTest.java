package org.AirAPI.Service;

import org.AirAPI.Entity.Schedule;
import org.AirAPI.Repository.SchduleRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    ScheduleService scheduleService;
    @Mock
    SchduleRepository schduleRepository;
    private static Schedule schedule;

    @BeforeEach
    public void init(){
        schedule = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .from("GMP") // 출발
                .to("GMP") // 도착
                .activity("OFF")
                .build();
     }

    @Test
    public void call(){
        // call findById(1)
        given(schduleRepository.save(schedule)).willReturn(schedule);

        Schedule schedulemock = scheduleService.findData(1);
        assertThat(schedule.getId(),Matchers.is(schedulemock.getId()));

    }
}