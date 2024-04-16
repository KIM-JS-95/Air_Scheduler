package org.air.service;

import org.air.config.AWStextrack;

import org.air.config.CustomCode;

import org.air.entity.*;
import org.air.repository.NationCodeRepository;
import org.air.repository.ScheduleRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import javax.transaction.Transactional;
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
    private ScheduleRepository scheduleRepository;
    @Autowired
    private NationCodeRepository nationCodeRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ScheduleDTO> getTodaySchedules(String startDate) {
        List<Schedule> schedules = scheduleRepository.findByDate(startDate);

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<ScheduleDTO> updatedStream = schedules.stream()
                .map(s -> {
                    String date = s.getDate();
                    if (s.getDate() == null) {
                        // date가 비어 있으면 이전 값을 사용
                        s.setDate(previousDateRef.get());
                    } else {
                        previousDateRef.set(date);
                    }
                    return s.toDTO();
                });
        // 스트림을 리스트로 변환 (optional)
        List<ScheduleDTO> updatedSchedules = updatedStream.collect(Collectors.toList());
        return updatedSchedules;
    }

    public List<ScheduleDTO> getSchedulesBydate(String sdate, String edate) {
        List<Schedule> schedules = scheduleRepository.findByDateBetween(sdate, edate);

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<ScheduleDTO> updatedStream = schedules.stream()
                .map(s -> {
                    String date = s.getDate();
                    if (s.getDate() == null) {
                        // date가 비어 있으면 이전 값을 사용
                        s.setDate(previousDateRef.get());
                    } else {
                        previousDateRef.set(date);
                    }
                    return s.toDTO();
                });

        List<ScheduleDTO> updatedSchedules = updatedStream.collect(Collectors.toList());
        return updatedSchedules;
    }

    public List<ScheduleDTO> getAllSchedules(String userid) {
        User user = userRepository.findByUserid(userid);
        List<Schedule> schedules = scheduleRepository.findByUserid(user);
        Stream<ScheduleDTO> updatedStream = schedules.stream()
                .map(s -> {
                    return s.toDTO();
                });
        return updatedStream.collect(Collectors.toList());
    }

    @Transactional
    public List<Schedule> schedule_save(List<Schedule> schedules, String userid) {

        User user = userRepository.findByUserid(userid);

        try {
            for (int i = 0; i < schedules.size(); i++) {
                if (schedules.get(i).getDate() == null && i > 0) {
                    schedules.get(i).setDate(schedules.get(i - 1).getDate());
                }
                if (schedules.get(i).getCi() == null && i > 0) {
                    schedules.get(i).setCi(schedules.get(i - 1).getCi());
                }
                schedules.get(i).setUserid(user); // 일정의 주인 추가
            }
            scheduleRepository.deleteAllByUserid(userid); // 기존 일정은 모두 삭제
            List<Schedule> result = scheduleRepository.saveAll(schedules);
            return result;
        } catch (RuntimeException e) {
            e.printStackTrace(); // 예외 로그 출력
            return new ArrayList<>();
        }
    }

    @Transactional
    public CustomCode modify(Long id, Schedule update_schedule) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
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
            return new CustomCode(StatusEnum.OK);
        } catch (Exception e) {
            return new CustomCode(StatusEnum.BAD_REQUEST);
        }
    }

    public boolean delete(String userid) {
        try {
            scheduleRepository.deleteAllByUserid(userid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Schedule> textrack(InputStream source) {

        HashMap<String, String> map = new HashMap<>();
        List<Block> list_block = new ArrayList<>();
        TextractClient textractClient = awstextrack.awsceesser();
        List<Block> block = AWStextrack.analyzeDoc(textractClient, source);

        block.forEach(callback -> {
            if (Objects.equals(callback.blockType().toString(), "WORD")) {
                map.put(callback.id(), callback.text());
            } else if (Objects.equals(callback.blockType().toString(), "CELL")) {
                list_block.add(callback);
            }
        });

        return AWStextrack.texttoEntity(map, list_block);
    }

    public Map<String, Map<String, String>> getNationCode() {
        List<NationCode> codes = nationCodeRepository.findAll();
        Map<String, Map<String, String>> codeCountryMap = convertToMap(codes);

        return codeCountryMap;
    }

    public Map<String, Map<String, String>> convertToMap(List<NationCode> nationCodes) {
        Map<String, Map<String, String>> codeCountryMap = new HashMap<>();

        for (NationCode nationCode : nationCodes) {
            Map<String, String> metadate = new HashMap<>();
            metadate.put("code", nationCode.getCode()); // code
            metadate.put("lat", nationCode.getLat()); // lat
            metadate.put("lon", nationCode.getLon()); // lon
            codeCountryMap.put(nationCode.getCountry(), metadate);
        }

        return codeCountryMap;

    }


}
