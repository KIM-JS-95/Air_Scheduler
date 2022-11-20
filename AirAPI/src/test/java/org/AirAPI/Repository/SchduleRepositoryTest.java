package org.AirAPI.Repository;

import org.AirAPI.Entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
class SchduleRepositoryTest {

    @Autowired
    private SchduleRepository schduleRepository;

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

    @Test
    public void save(){

        schduleRepository.saveAll(scheduleList);

    }
}