package org.air.repository;

import org.air.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    public void init() {
        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("02Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule3 = Schedule.builder()
                .id(3L)
                .date("03Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();
        List<Schedule> l = new ArrayList<>();

        l.add(schedule1);
        l.add(schedule2);
        l.add(schedule3);

        scheduleRepository.saveAll(l);
    }

    @Test
    public void save_test() {

        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("01Nov23")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
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
    public void call_schedule() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy");
        String startdateString = "01Nov22";
        String enddateString = "03Nov22";

        List<Schedule> schedules = scheduleRepository.findByDateBetween(startdateString, enddateString);

        assertThat(schedules.get(2).getDate(),is("03Nov22"));

    }
}