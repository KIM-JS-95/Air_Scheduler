package org.air.service;

import org.air.config.AWStextrack;
import org.air.config.CustomErrors;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Configuration
public class ScheduleService {
    @Autowired
    private AWStextrack awstextrack;
    @Autowired
    private ScheduleRepository schduleRepository;

    // get 3 dats schedules
    public List<Schedule> getSchedules(String startDate, String endDate) {
        startDate = "23Oct23";
        Schedule schedule = schduleRepository.findByDate(startDate);

        Long start_id = schedule.getId();
        Long end_id = start_id + 3L;

        return schduleRepository.findByIdBetween(start_id, end_id);
    }

    public boolean schedulesCheck(String s_date) {
        return schduleRepository.existsByDate(s_date);
    }

    // get 3 dats schedules
    public List<Schedule> getALlSchedules() {
        List<Schedule> schedule = schduleRepository.findAll();
        return schedule;
    }

    // SAVE
    public boolean schedule_save(List<Schedule> schedules) {
        try {
            System.out.println("schedules.size(): " + schedules.size());
            schduleRepository.saveAll(schedules);
            return true;
        } catch (NullPointerException e) {
            throw e;
        }
    }


    // 형식이 또 바뀌었었어요! 극혐이에요!
    @Transactional
    public Schedule modify(Long id, Schedule update_schedule) {
        Schedule schedule = schduleRepository.findById(id).orElseThrow();
        try {
            // Dirty checking 은 전체 필드를 update 하는 방식을 기본으로 사용함
            schedule.setDate(update_schedule.getDate());
            schedule.setPairing(update_schedule.getPairing());
            schedule.setDc(update_schedule.getDc());
            schedule.setCi(update_schedule.getCi());
            schedule.setCo(update_schedule.getCo());
            schedule.setActivity(update_schedule.getActivity());
            schedule.setCntFrom(update_schedule.getCntFrom());
            schedule.setStd(update_schedule.getStd());
            schedule.setCntTo(update_schedule.getCntTo());
            schedule.setSta(update_schedule.getSta());
            schedule.setAchotel(update_schedule.getAchotel());
            schedule.setBlk(update_schedule.getBlk());
            return schedule;
        } catch (Exception e) {
            throw new CustomErrors(StatusEnum.BAD_REQUEST);
        }
    }

    public boolean delete() {
        try {
            schduleRepository.truncateTable();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // GET JPG -> AWS textreck -> user Check
    // 데이터를 획득하고 유저에게 검증 후 'schedule_save' 함수로 저장할꺼야
    public List<Schedule> textrack(InputStream source) {
        HashMap<String, String> map = new HashMap<>();
        List<Block> list_block = new ArrayList<>();
        TextractClient textractClient = awstextrack.awsceesser();
        List<Block> block = awstextrack.analyzeDoc(textractClient, source);

        block.forEach(callback -> {
            if (callback.blockType().toString() == "WORD") {
                map.put(callback.id(), callback.text());
            } else if (callback.blockType().toString() == "CELL") {
                list_block.add(callback);
            }
        });

        return awstextrack.texttoEntity(map, list_block);
    }
}
