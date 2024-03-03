package org.air.repository;

import org.air.entity.Schedule;
import org.air.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// @Springboot -> @DataJpaTest 으로 경량화 진행
@SpringBootTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void init() {
        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .stdL("0000") // 출발 시간
                .staL("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("02Nov22")
                .stdL("0000") // 출발 시간
                .staL("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        List<Schedule> l = new ArrayList<>();
        l.add(schedule1);
        l.add(schedule2);

        //scheduleRepository.saveAll(l);
    }

    @Test
    @Disabled
    public void save_test() {

        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .stdL("0000") // 출발 시간
                .staL("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("01Nov23")
                .stdL("0000") // 출발 시간
                .staL("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        List<Schedule> schedule_list = new ArrayList<>();
        schedule_list.add(schedule1);
        schedule_list.add(schedule2);
        scheduleRepository.saveAll(schedule_list);

    }

    @Test
    public void call_schedule(){
        String startdateString = "01Nov23";
        String enddateString = "03Nov23";
        List<Schedule> schedules = scheduleRepository.findByDateBetween(startdateString, enddateString);
        assertThat(schedules.get(1).getDate(),is("03Nov23"));

        schedules.stream().forEach(element -> {
            // 요소에 대한 작업 수행
            System.out.println(element);
        });
    }

    @Test
    @DisplayName("find All schedule")
    public void scheduleAll(){
        List<Schedule> schedules = scheduleRepository.findAll();

        schedules.stream().forEach(element -> {
            // 요소에 대한 작업 수행
            System.out.println(element);
        });
    }
}