package org.air.service;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Schedule;
import org.air.entity.json.Blocks;
import org.air.entity.json.Jsonschedules;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


@Slf4j
@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;


    private List<Blocks> block;

    @Test
    @Disabled
    @DisplayName("save test")
    public void save() throws IOException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        List<Blocks> list_block = new ArrayList<>();


        Jsonschedules jsonschedules = new Jsonschedules();
        block = jsonschedules.readJsonFile();
        block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            } else if (callback.getBlockType().toString().equals("CELL")) {
                list_block.add(callback);
            }
        });

        List<Schedule> schedules_test = jsonschedules.getschedules(map, list_block);

        schedules_test.forEach(callback -> {
            log.debug(callback.toString());
        });

    }

    @Test
    @DisplayName("select All Date")
    public void getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        assertThat(schedules.get(0).getDate(), is("01Nov23"));
    }

    @Test
    @DisplayName("select By Date")
    public void getSchedulesBydate() {
        String sdate = "01Nov223";
        String edate = "03Nov23";
        List<Schedule> schedules = scheduleService.getSchedulesBydate(sdate, edate);

        log.info(schedules.get(0).toString());
        assertAll("Schedules by Date",
                () -> assertThat(schedules, notNullValue()),
                () -> assertThat(schedules, not(empty())),
                () -> assertThat(schedules, hasItem(hasProperty("date", equalTo(edate))))
        );

    }
}