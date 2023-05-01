package org.AirAPI.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
        Schedule schedule = new Schedule();

        Float y1 = 0f;
        String sample = "";
        for (int i = 17; i < list.size(); i++) {
            String text = list.get(i).getText();
            Float x = list.get(i).getGeometry().getPolygon()[0].getX();
            Float y2 = list.get(i).getGeometry().getPolygon()[0].getY();
            if (x >= 0.63274595f) {
                System.out.println(sample);
                sample = "";
            }
            sample +=text;
            sample += " || ";
/*
            if (0f < x && x < scheduleIndex.get(1)) {
                schedule.setDate(text);
            } else if (scheduleIndex.get(1) < x && x < scheduleIndex.get(2)) {
                schedule.setPairing(text);
            } else if (scheduleIndex.get(2) < x && x < scheduleIndex.get(3)) {
                schedule.setDc(text);
            } else if (scheduleIndex.get(3) < x && x < scheduleIndex.get(4)) {
                schedule.setCi(text);
            } else if (scheduleIndex.get(4) < x && x < scheduleIndex.get(5)) {
                schedule.setCo(text);
            } else if (scheduleIndex.get(5) < x && x < scheduleIndex.get(6)) {
                schedule.setActivity(text);
            } else if (scheduleIndex.get(6) < x && x < scheduleIndex.get(7)) {
                schedule.setCnt_from(text);
            } else if (scheduleIndex.get(7) < x && x < scheduleIndex.get(8)) {
                schedule.setStd(text);
            } else if (scheduleIndex.get(8) < x && x < scheduleIndex.get(9)) {
                schedule.setCnt_to(text);
            } else if (scheduleIndex.get(9) < x && x < scheduleIndex.get(10)) {
                schedule.setStd(text);
            }
    */

        }
    }

    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();//.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //File file = new File("C:\\Users\\JAESEUNG\\IdeaProjects\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
        File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse_test.json");
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