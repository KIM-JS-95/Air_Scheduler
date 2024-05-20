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

    @Autowired
    private FcmServiceImpl fcmService;

    @Transactional
    public List<ScheduleDTO> getTodaySchedules(String userid, String startDate) {
        User user = userRepository.findByUserid(userid);
        String auth = user.getAuthority().getAuthority();

        List<Schedule> schedules = new ArrayList<>();
        if(auth.equals("USER")) {
            schedules = scheduleRepository.findByUserAndDate(user, startDate);
        }else if(auth.equals("FAMILY")){
            User family = userRepository.findByPilotcode(user.getFamily());
            schedules = scheduleRepository.findByUserAndDate(family, startDate);
        }

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<ScheduleDTO> updatedStream = schedules.stream()
                .map(s -> {
                    String date = s.getDate();
                    if (s.getDate() == null) {
                        s.setDate(previousDateRef.get()); // date가 비어 있으면 이전 값을 사용
                    } else {
                        previousDateRef.set(date);
                    }
                    return s.toDTO();
                });

        List<ScheduleDTO> updatedSchedules = updatedStream.collect(Collectors.toList());
        return updatedSchedules;
    }

    // 이건 당장 구분 안해줘도 될듯
    public ScheduleDTO getViewSchedule(String userid, Long id){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
        return schedule.toDTO();
    }
    public List<ScheduleDTO> getAllSchedules(String userid) {
        User user = userRepository.findByUserid(userid);
        String auth = user.getAuthority().getAuthority();

        List<Schedule> schedules = new ArrayList<>();
        if(auth.equals("USER")) {
            schedules = scheduleRepository.findByUserPilotcode(user.getPilotcode());
        }else if(auth.equals("FAMILY")){
            schedules = scheduleRepository.findByUserPilotcode(user.getFamily());
        }
        Stream<ScheduleDTO> updatedStream = schedules.stream()
                .map(s -> {
                    return s.toDTO();
                });

        return updatedStream.collect(Collectors.toList());
    }

    // -------------------------- 일정 수정 삭제 저장 --------------------------
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
                schedules.get(i).setUser(user); // 일정의 주인 추가
            }
            scheduleRepository.deleteAllByUserPilotcode(userid); // 기존 일정은 모두 삭제
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

            String title = "🛩️ 비행 일정이 변경되었어요! 🛩️";
            String body = "- 날짜: "+update_schedule.getDate()+"\n- 목적지: "+ update_schedule.getCntTo();

            FcmSendDto fcmSendDto = FcmSendDto.builder()
                    .token(schedule.getUser().getDevice_token())
                    .title(title)
                    .body(body)
                    .build();

            // 알림 보내기
            fcmService.sendMessageTo(fcmSendDto);

            return new CustomCode(StatusEnum.OK);
        } catch (Exception e) {
            return new CustomCode(StatusEnum.BAD_REQUEST);
        }
    }

    // 파일럿 일정 전체 삭제
    public boolean delete(String userid) {
        try {
            scheduleRepository.deleteAllByUserPilotcode(userid);
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
