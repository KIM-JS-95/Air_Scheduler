package org.AirAPI.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AirAPI.entity.Schedule;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BlockTest {

    @Test
    public void setEntity() throws IOException {
        List<Float> scheduleIndex = new ArrayList<>();
        scheduleIndex.add(0.004148122f);
        scheduleIndex.add(0.08701322f);
        scheduleIndex.add(0.17003645f);
        scheduleIndex.add(0.21753336f);
        scheduleIndex.add(0.27800912f);
        scheduleIndex.add(0.337937f);
        scheduleIndex.add(0.4218124f);
        scheduleIndex.add(0.48183724f);
        scheduleIndex.add(0.5422942f);
        scheduleIndex.add(0.60274595f);
        scheduleIndex.add(0.6360592246055603f);
        scheduleIndex.add(0.9166650176048279f);

        List<Blocks> block = readJsonFile();
        ex2_test(scheduleIndex, block);
    }

    public void ex2_test(List<Float> scheduleIndex, List<Blocks> list) {
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Schedule schedule = new Schedule();
            Blocks block = list.get(i);
            if (block.getColumnIndex() == 1) {
                schedule.setDate(block.getChildText());
            }
        }
    }

    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        //File file = new File("C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
        File file = new File("C:\\Users\\LKY\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
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