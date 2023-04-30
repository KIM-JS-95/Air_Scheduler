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

        List<Blocks> block = readJsonFile();
        ex2_test(scheduleIndex, block);
    }

    public void ex2_test(List<Float> scheduleIndex, List<Blocks> list) {
        Schedule schedule = new Schedule();
        String date = "";
        String pairing = "";
        String dc = "";
        String ci = "";
        String co = "";
        String activity = "";
        String cnt_from = "";
        String std = "";
        String cnt_to = "";
        String sta = "";
        Float y1 = 0f;
        for (int i = 17; i < list.size(); i++) {
            String text = list.get(i).getText();
            Float x = list.get(i).getGeometry().getPolygon()[0].getX();
            Float y2 = list.get(i).getGeometry().getPolygon()[0].getY();

            if (0f < x && x < scheduleIndex.get(1)) {
                date = text;
            } else if (scheduleIndex.get(1) < x && x < scheduleIndex.get(2)) {
                pairing = text;
            } else if (scheduleIndex.get(2) < x && x < scheduleIndex.get(3)) {
                dc = text;
            } else if (scheduleIndex.get(3) < x && x < scheduleIndex.get(4)) {
                ci = text;
            } else if (scheduleIndex.get(4) < x && x < scheduleIndex.get(5)) {
                co = text;
            } else if (scheduleIndex.get(5) < x && x < scheduleIndex.get(6)) {
                activity = text;
            } else if (scheduleIndex.get(6) < x && x < scheduleIndex.get(7)) {
                cnt_from = text;
            } else if (scheduleIndex.get(7) < x && x < scheduleIndex.get(8)) {
                std = text;
            } else if (scheduleIndex.get(8) < x && x < scheduleIndex.get(9)) {
                cnt_to = text;
            } else if (scheduleIndex.get(9) < x && x < scheduleIndex.get(10)) {
                sta = text;
            }
            if (y2 - y1 > 0.01f) {
                y1 = y2;
                schedule.setDate(date);
                schedule.setPairing(pairing);
                schedule.setDc(dc);
                schedule.setCi(ci);
                schedule.setCo(co);
                schedule.setActivity(activity);
                schedule.setCnt_from(cnt_from);
                schedule.setStd(std);
                schedule.setCnt_to(cnt_to);
                schedule.setSta(sta);
                System.out.println(schedule.toString());
            }
        }
    }

    @Test
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