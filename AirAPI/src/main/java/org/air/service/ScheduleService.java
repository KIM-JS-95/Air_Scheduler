package org.air.service;

import org.air.config.AWStextrack;
import org.air.entity.Schedule;
import org.air.repository.ScheduleRepository;
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
    private ScheduleRepository schduleRepository;

    // get 3 dats schedules

    public List<Schedule> getSchedules(String s_date, String e_date) {
        return schduleRepository.existsByDate(s_date) ? schduleRepository.findByDateBetween(s_date, e_date) : null;
    }

    public boolean schedulesCheck(String s_date) {
        return schduleRepository.existsByDate(s_date);
    }

    // GET JPG -> AWS textreck -> user Check
    // 데이터를 획득하고 유저에게 검증 후 'schedule_save' 함수로 저장할꺼야
    public List<Schedule> textrack(InputStream source) {
        HashMap<String, String> map = new HashMap<>();
        TextractClient textractClient = awstextrack.awsceesser();
        List<Block> block = awstextrack.analyzeDoc(textractClient, source);
        block.forEach(callback -> {
            // 아 왜또 안되는데...
            System.out.println(callback.text());
            if (callback.blockType().equals("WORD")) {
                map.put(callback.id(), callback.text());
            }
        });
        return awstextrack.texttoEntity(map, block);
    }

    // SAVE
    public boolean schedule_save(List<Schedule> schedules) {
        try {
            List<Schedule> re = schduleRepository.saveAll(schedules);
            System.out.println(re.get(0));
            return true;
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public boolean modify(int i, Schedule update_schedule) {
        Schedule schedule = schduleRepository.findById(i);
        try {
            // 변경감지
            schedule.setDate(update_schedule.getDate());
            schedule.setPairing(update_schedule.getPairing());
            schedule.setDc(update_schedule.getDc());
            schedule.setCi(update_schedule.getCi());
            schedule.setCo(update_schedule.getCo());
            schedule.setActivity(update_schedule.getActivity());
            schedule.setCnt_from(update_schedule.getCnt_from());
            schedule.setStd(update_schedule.getStd());
            schedule.setCnt_to(update_schedule.getCnt_to());
            schedule.setSta(update_schedule.getSta());
            schedule.setAchotel(update_schedule.getAchotel());
            schedule.setBlk(update_schedule.getBlk());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        return true;
    }
}
