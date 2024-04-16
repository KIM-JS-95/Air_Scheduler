package org.air.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.air.entity.Schedule;
import org.air.entity.ScheduleDTO;
import org.air.entity.json.Blocks;
import org.air.entity.json.Jsonschedules;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
    @DisplayName("save test")
    public void save() throws IOException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        List<Blocks> list_block = new ArrayList<>();

        //Jsonschedules jsonschedules = new Jsonschedules();
        block = readJsonFile();
        block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            } else if (callback.getBlockType().toString().equals("CELL")) {
                list_block.add(callback);
            }
        });

        List<Schedule> schedules_test = getschedules(map, list_block);

        schedules_test.forEach(callback -> {
            log.info(callback.toString());
        });

    }

    @Test
    @DisplayName("select All Date")
    public void getAllSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules("001200");
        assertThat(schedules.get(0).getDate(), is("01Nov24"));
    }

    @Test
    @DisplayName("select By Date")
    public void getSchedulesBydate() {
        String sdate = "01Nov223";
        String edate = "03Nov23";
        List<ScheduleDTO> schedules = scheduleService.getSchedulesBydate(sdate, edate);

        log.info(schedules.get(0).toString());
        assertAll("Schedules by Date",
                () -> assertThat(schedules, notNullValue()),
                () -> assertThat(schedules, not(empty())),
                () -> assertThat(schedules, hasItem(hasProperty("date", equalTo(edate))))
        );

    }

    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        //File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        File file = new File("C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        System.out.println(file.toString());
        try {
            List<Blocks> entities = objectMapper.readValue(file, new TypeReference<>() {
            });
            return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Schedule> getschedules(HashMap<String, String> map, List<Blocks> list) throws ParseException {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBlockType().equals("CELL")) {
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
                        schedule.setCntFrom(map.get(ids[0]));
                    } else if (index == 7) {
                        schedule.setStdL(map.get(ids[0]));
                    } else if (index == 8) {
                        schedule.setCntTo(map.get(ids[0]));
                    } else if (index == 9) {
                        schedule.setStaB(map.get(ids[0]));
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
}