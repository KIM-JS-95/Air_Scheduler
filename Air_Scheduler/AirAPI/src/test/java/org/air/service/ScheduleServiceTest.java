package org.air.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.air.entity.*;
import org.air.entity.json.Blocks;
import org.air.repository.AuthorityRepository;
import org.air.repository.NationCodeRepository;
import org.air.repository.ScheduleRepository;
import org.air.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


@Slf4j
@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private NationCodeRepository nationCodeRepository;

    private List<Blocks> block;

    @Autowired
    AuthorityRepository authorityRepository;

    @Test
    @Transactional
    public void cron() throws FirebaseMessagingException {
        Authority authority = Authority.builder()
                .id(2L)
                .authority("USER")
                .build();

        String title = "🛩️ 일정을 아직 등록하지 않으셨나요? 🛩️";
        String body=" 일정을 등록하고 다음달 일정을 확인해 보세요.";
        List<User> users = userRepository.findByAuthority(authority);

        List<String> deviceTokens = users.stream()
                .map(User::getDevice_token)
                .collect(Collectors.toList());

        if (!deviceTokens.isEmpty()) {
            MulticastMessage fcm = MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .addAllTokens(deviceTokens)
                    .build();

            FirebaseMessaging.getInstance().sendMulticast(fcm);
        }
    }

    @Test
    @DisplayName("USER - get today schedule")
    public void getTodaySchedules() {
        User user = userRepository.findByUserid("6432");
        List<Schedule> schedules = scheduleRepository.findByUserAndDate(user, "03May24");

        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<Schedule> updatedStream = schedules.stream().map(s -> {
            String date = s.getDate();
            if (s.getDate() == null) {
                s.setDate(previousDateRef.get()); // date가 비어 있으면 이전 값을 사용
            } else {
                previousDateRef.set(date);
            }
            return s;
        });
        List<Schedule> updatedSchedules = updatedStream.collect(Collectors.toList());
        Map<String, Map<String, String>> code = getNationCode();

        List<FlightData> list = generateFlightData(updatedSchedules, code);
        assertThat(list.get(0).getStab(), is("1810"));
    }

    @Test
    @DisplayName("Family - get today schedule")
    public void getTodaySchedules_family() {
        User user = userRepository.findByUserid("2");
        String auth = user.getAuthority().getAuthority();

        if (auth.equals("FAMILY")) {
            User family = userRepository.findByUserid(user.getFamily());
            List<Schedule> schedules = scheduleRepository.findByUserAndDate(family, "20May24");
            System.out.println(schedules);
        }
    }

    @Test
    public void getViewSchedule() {
        Long id = 92L;
        Schedule schedule = scheduleRepository.findById(id).orElseThrow();
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule);

        Map<String, Map<String, String>> code = getNationCode();
        List<FlightData> list = generateFlightData(schedules, code);
        assertThat(list.get(0).getStab(), is("1810"));
    }

    @Test
    @DisplayName("select All Date")
    public void getAllSchedules() {

        User user = userRepository.findByUserid("6432");
        String auth = user.getAuthority().getAuthority();

        List<Schedule> schedules = new ArrayList<>();
        if (auth.equals("USER")) {
            schedules = scheduleRepository.findByUserUserid(user.getUserid());
        } else if (auth.equals("FAMILY")) {
            schedules = scheduleRepository.findByUserUserid(user.getFamily());
        }
        AtomicReference<String> previousDateRef = new AtomicReference<>();
        Stream<Schedule> updatedStream = schedules.stream().map(s -> {
            String date = s.getDate();
            if (s.getDate() == null) {
                s.setDate(previousDateRef.get()); // date가 비어 있으면 이전 값을 사용
            } else {
                previousDateRef.set(date);
            }
            return s;
        });

        List<Schedule> updatedSchedules = updatedStream.collect(Collectors.toList());
        Map<String, Map<String, String>> code = getNationCode();
        List<FlightData> list = generateFlightData(updatedSchedules, code);
        System.out.println(list.get(4));
    }


    // -------------------------- 일정 수정 삭제 저장 --------------------------


    @Test
    @DisplayName("save test")
    public void save() throws IOException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        List<Blocks> list_block = new ArrayList<>();

        //Jsonschedules jsonschedules = new Jsonschedules();
        block = readJsonFile();
        block.forEach(callback -> {
            if (callback.getBlockType().equals("WORD")) {
                map.put(callback.getId(), callback.getText());
            } else if (callback.getBlockType().toString().equals("CELL")) {
                list_block.add(callback);
            }
        });

        List<Schedule> schedules_test = getschedules(map, list_block);

        schedules_test.forEach(callback -> {
            log.info(callback.toString());
        });

    }


    public List<FlightData> generateFlightData(List<Schedule> sList, Map<String, Map<String, String>> codes) {
        List<FlightData> flightDataList = new ArrayList<>();

        for (Schedule s : sList) {
            FlightData flightData = FlightData.builder()
                    .departureShort(s.getCntFrom())
                    .departure(codes.getOrDefault(s.getCntFrom(), Collections.emptyMap()).getOrDefault("code", "Unknown"))
                    .date(s.getDate())
                    .destinationShort(s.getCntTo())
                    .destination(codes.getOrDefault(s.getCntTo(), Collections.emptyMap()).getOrDefault("code", "Unknown"))
                    .flightNumber(s.getPairing())
                    .stal(s.getStal())
                    .stab(s.getStab())
                    .stdl(s.getStdl())
                    .stdb(s.getStdb())
                    .activity(s.getActivity())
                    .id(s.getId())
                    .ci(s.getCi())
                    .co(s.getCo())
                    .lat(codes.getOrDefault(s.getCntTo(), Collections.emptyMap()).getOrDefault("lat", "unknown"))
                    .lon(codes.getOrDefault(s.getCntTo(), Collections.emptyMap()).getOrDefault("lon", "unknown"))
                    .build();
            flightDataList.add(flightData);
        }

        return flightDataList;
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



    public List<Blocks> readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        //File file = new File("D:\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        File file = new File("C:\\Users\\KIMJAESUNG\\Air_Scheduler\\AirAPI\\src\\main\\resources\\analyzeDocResponse.json");
        System.out.println(file.toString());
        try {
            List<Blocks> entities = objectMapper.readValue(file, new TypeReference<>() {
            });
            return entities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Schedule> getschedules(HashMap<String, String> map, List<Blocks> list) throws ParseException {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBlockType().equals("CELL")) {
                Blocks block = list.get(i);
                int index = block.getColumnIndex();
                if (list.get(i).getRowIndex() == 1 || index == 2) continue;
                if (index == 11) {
                    schedules.add(schedule);
                    schedule = new Schedule();
                }
                if (block.getRelationships() != null) {
                    String[] ids = block.getRelationships()[0].getIds();
                    if (index == 1) {
                        if (ids.length == 1) {
                            if (isDateValid(map.get(ids[0]))) {
                                schedule.setDate(map.get(ids[0]));
                            } else {
                                schedule.setPairing(map.get(ids[0]));
                            }
                        } else {
                            schedule.setDate(map.get(ids[0]));
                            schedule.setPairing(map.get(ids[1]));
                        }
                    } else if (index == 3) {
                        schedule.setDc(map.get(ids[0]));
                    } else if (index == 4) {
                        schedule.setCi(map.get(ids[0]));
                    } else if (index == 5) {
                        schedule.setActivity(map.get(ids[0]));
                    } else if (index == 6) {
                        schedule.setCntFrom(map.get(ids[0]));
                    } else if (index == 7) {
                        schedule.setStdl(map.get(ids[0]));
                    } else if (index == 8) {
                        schedule.setCntTo(map.get(ids[0]));
                    } else if (index == 9) {
                        schedule.setStab(map.get(ids[0]));
                    } else if (index == 10) {
                        String hotel = "";
                        for (int j = 0; j < ids.length; j++) {
                            hotel += map.get(ids[j]);
                        }
                        schedule.setAchotel(hotel);
                    } else if (index == 11) {
                        schedule.setBlk(map.get(ids[0]));
                    }
                }
            }
        }
        return schedules;
    }

    public static boolean isDateValid(String dateString) {
        try {
            String dateFormatPattern = "^\\d{2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\d{2}$";
            return Pattern.matches(dateFormatPattern, dateString);
        } catch (Exception e) {
            return false;
        }
    }
}