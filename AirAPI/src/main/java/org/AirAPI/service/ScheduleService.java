package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.repository.SchduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private AWStextrack awstextrack;

    @Autowired
    private SchduleRepository schduleRepository;

    public Schedule findData(int id) {
        return schduleRepository.findById(id).orElseThrow();
    }

    public List<Schedule> textrack(InputStream source) {
        TextractClient textractClient = awstextrack.awsceesser();
        List<Block> block = awstextrack.analyzeDoc(textractClient, source);
        HashMap<String, String> map = new HashMap<>();

        block.forEach(callback -> {
            if (callback.blockType().equals("WORD")) {
                map.put(callback.id(), callback.text());
            }
        });
        return awstextrack.TexttoEntity(map,block);
    }

    public boolean schedule_save(List<Schedule> schedules){
        try {
            schduleRepository.saveAll(schedules);
            return true;
        }catch (Exception e){
            // 에러 행들링
            return false;
        }
    }
}
