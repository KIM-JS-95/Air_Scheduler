package org.air.service;

import org.air.config.AWStextrack;
import org.air.config.CustomErrors;
import org.air.entity.NationCode;
import org.air.entity.Schedule;
import org.air.entity.StatusEnum;
import org.air.repository.NationCodeRepository;
import org.air.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Configuration
public class ScheduleService {
    @Autowired
    private AWStextrack awstextrack;
    @Autowired
    private ScheduleRepository schduleRepository;

    @Autowired
    private NationCodeRepository nationCodeRepository;

    public List<Schedule> getSchedules(String startDate, String endDate) {
        List<Schedule> schedule = schduleRepository.findByDate(startDate);

        Long start_id = schedule.get(0).getId();
        Long end_id = start_id + 3L;
        List<Schedule> schedules= schduleRepository.findByIdBetween(start_id, end_id);

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<Schedule> updatedStream = schedules.stream()
                .map(s -> {
                    String date = s.getDate();
                    if (s.getDate()==null) {
                        // date가 비어 있으면 이전 값을 사용
                        s.setDate(previousDateRef.get());
                    } else {
                        previousDateRef.set(date);
                    }
                    return s;
                });
        // 스트림을 리스트로 변환 (optional)
        List<Schedule> updatedSchedules = updatedStream.collect(Collectors.toList());
        return updatedSchedules;
    }

    public List<Schedule> getTodaySchedules(String startDate) {
        List<Schedule> schedules = schduleRepository.findByDate(startDate);

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<Schedule> updatedStream = schedules.stream()
                .map(s -> {
                    String date = s.getDate();
                    if (s.getDate()==null) {
                        // date가 비어 있으면 이전 값을 사용
                        s.setDate(previousDateRef.get());
                    } else {
                        previousDateRef.set(date);
                    }
                    return s;
                });
        // 스트림을 리스트로 변환 (optional)
        List<Schedule> updatedSchedules = updatedStream.collect(Collectors.toList());
        return updatedSchedules;
    }

    public Map<String, Map<String, String>> getNationCode(){
        List<NationCode> codes = nationCodeRepository.findAll();
        Map<String, Map<String, String>> codeCountryMap = convertToMap(codes);

        return codeCountryMap;
    }
    public Map<String, Map<String, String>> convertToMap(List<NationCode> nationCodes) {
        Map<String, Map<String, String>> codeCountryMap = new HashMap<>();

        for (NationCode nationCode : nationCodes) {
            Map<String, String> metadate =new HashMap<>();
            metadate.put("code", nationCode.getCode()); // code
            metadate.put("lat",nationCode.getLat()); // lat
            metadate.put("lon",nationCode.getLon()); // lon
            codeCountryMap.put(nationCode.getCountry(), metadate);
        }

        return codeCountryMap;
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
            schedule.setStdL(update_schedule.getStdL());
            schedule.setStdB(update_schedule.getStdB());

            schedule.setCntTo(update_schedule.getCntTo());
            schedule.setStaL(update_schedule.getStaL());
            schedule.setStaB(update_schedule.getStaB());

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
    public List<Schedule> textrack(InputStream source){
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
