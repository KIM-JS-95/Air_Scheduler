package org.AirAPI.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * AWS Textrect 요금이 너무 많이 나와 급하게 만든 테스트 전용 클래스 <br>
 * Since 1.0 <br>
 * Author: 김재성 <br>
 */
@Component
public class Jsonschedules {

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
