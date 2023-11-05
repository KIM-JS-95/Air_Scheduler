package org.air.repository;

import org.air.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
class SchduleRepositoryTest {

    @Autowired
    private ScheduleRepository schduleRepository;

    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule schedule3;
    List<Schedule> scheduleList = new ArrayList<>();

    @BeforeEach
    public void init() {

        schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .stdL("0000") // 출발 시간
                .staL("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        scheduleList.add(schedule3);
    }

    @Test
    public void save(){

        schduleRepository.saveAll(scheduleList);

    }
}