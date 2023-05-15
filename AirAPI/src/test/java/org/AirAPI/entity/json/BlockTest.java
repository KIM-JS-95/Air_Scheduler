package org.AirAPI.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Schedules;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BlockTest {

    @Autowired
    private ScheduleService service;
    @Test
    public void setEntity() throws IOException {
        List<Blocks> block = readJsonFile();
        ex2_test(block);
    }

    public void ex2_test(List<Blocks> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRowIndex() != 1) {
                Blocks block = list.get(i);
                int index = block.getColumnIndex();
                String chileText = block.getChildText();
                if (index == 1) {
                    schedule.setDate(chileText);
                } else if (index == 2) {
                    schedule.setPairing(chileText);
                } else if (index == 3) {
                    schedule.setDc(chileText);
                } else if (index == 4) {
                    schedule.setCi(chileText);
                } else if (index == 5) {
                    String[] units = chileText.split(" ");
                    if (units.length == 1) {
                        schedule.setActivity(units[0]);
                    } else {
                        schedule.setCo(units[0]);
                        schedule.setActivity(units[1]);
                    }
                } else if (index == 6) {
                    schedule.setCnt_from(chileText);
                } else if (index == 7) {
                    schedule.setStd(chileText);
                } else if (index == 8) {
                    schedule.setCnt_to(chileText);
                } else if (index == 9) {
                    schedule.setSta(chileText);
                } else if (index == 10) {
                    schedule.setAchotel(chileText);
                } else if (index == 11) {
                    schedule.setBlk(chileText);
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
            }
        }
        schedules.forEach((n) -> System.out.println(n));
    }

    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        File file = new File("C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
        try {
            List<Blocks> entities = objectMapper.readValue(file, new TypeReference<List<Blocks>>() {
            });
            return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}