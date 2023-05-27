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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

class BlockTest {

    @Autowired
    private ScheduleService service;

    @Test
    @DisplayName("dummy Entity- test")
    public void setEntity() throws IOException, ParseException {
        List<Blocks> block = readJsonFile();
        HashMap<String, String> map = new HashMap<>();
        block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            }
        });
        ex2_test(map, block);
    }

    public void ex2_test(HashMap<String, String> map, List<Blocks> list) throws ParseException {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBlockType().equals("CELL")) {
                Blocks block = list.get(i);
                int index = block.getColumnIndex();
                if (list.get(i).getRowIndex() == 1 || index == 2) continue;
                if(index==11){
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
                if (block.getRelationships()!=null) {
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
                        schedule.setCnt_from(map.get(ids[0]));
                    } else if (index == 7) {
                        schedule.setStd(map.get(ids[0]));
                    } else if (index == 8) {
                        schedule.setCnt_to(map.get(ids[0]));
                    } else if (index == 9) {
                        schedule.setSta(map.get(ids[0]));
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
        schedules.forEach(n->System.out.println(n));
    }

    public static boolean isDateValid(String dateString) {
        try {
            String dateFormatPattern = "^\\d{2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\d{2}$";
            return Pattern.matches(dateFormatPattern, dateString);
        } catch (Exception e) {
            return false;
        }
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