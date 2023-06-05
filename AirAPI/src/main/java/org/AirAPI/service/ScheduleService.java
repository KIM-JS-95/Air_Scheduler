package org.AirAPI.service;

import org.AirAPI.config.AWStextrack;
import org.AirAPI.entity.Schedule;
import org.AirAPI.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.io.InputStream;
import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private AWStextrack awstextrack;
    @Autowired
    private ScheduleRepository schduleRepository;

    // get 3 dats schedules
    public List<Schedule> findData(int id) {
        return schduleRepository.findById(id);
    }
    // GET JPG -> AWS textreck -> user Check
    // 데이터를 획득하고 유저에게 검증 후 'schedule_save' 함수로 저장할꺼야
    public List<Schedule> textrack(InputStream source) {
        HashMap<String, String> map = new HashMap<>();
        TextractClient textractClient = awstextrack.awsceesser();
        List<Block> block = awstextrack.analyzeDoc(textractClient, source);
        block.forEach(callback -> {
            if (callback.blockType().equals("WORD")) {
                map.put(callback.id(), callback.text());
            }
        });
        return awstextrack.texttoEntity(map,block);
    }

    // SAVE
    public boolean schedule_save(List<Schedule> schedules){
        try {
            schduleRepository.saveAll(schedules);
            return true;
        }catch (NullPointerException e){
            throw e;
        }
    }

    public boolean modify(){

    }

    public boolean delete(){

    }
}
