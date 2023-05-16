package org.AirAPI.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.AirAPI.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Schedules;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BlockTest {

    @Autowired
    private ScheduleService service;
    @Test
    @DisplayName("dummy Entity- test")
    public void setEntity() throws IOException {
        List<Blocks> block = readJsonFile();
        HashMap<String, String> map = new HashMap<>();

        block.forEach(callback -> {
            if (callback.getBlockType() == "WORD") {
                map.put(callback.getId(), callback.getText());
            }
        });


        ex2_test(map, block);
    }

    public void ex2_test(HashMap<String, String> map, List<Blocks> list) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBlockType().equals("CELL")) {
                if (list.get(i).getRowIndex() == 1) continue;
                Blocks block = list.get(i);
                int index = block.getColumnIndex();
                String[] ids = block.getRelationships()[0].getIds();
                String id = map.get(ids);
                if (index == 1) {
                    schedule.setDate(id);
                } else if (index == 2) {
                    schedule.setPairing(id);
                } else if (index == 3) {
                    schedule.setDc(id);
                } else if (index == 4) {
                    schedule.setCi(id);
                } else if (index == 5) {
                    String[] units = id.split(" ");
                } else if (index == 6) {
                    schedule.setCnt_from(id);
                } else if (index == 7) {
                    schedule.setStd(id);
                } else if (index == 8) {
                    schedule.setCnt_to(id);
                } else if (index == 9) {
                    schedule.setSta(id);
                } else if (index == 10) {
                    schedule.setAchotel(id);
                } else if (index == 11) {
                    schedule.setBlk(id);
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
        //File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        File file = new File("C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
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