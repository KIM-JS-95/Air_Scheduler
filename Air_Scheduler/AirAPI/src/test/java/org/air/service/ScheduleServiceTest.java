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
import org.air.repository.AuthorityRepository;
import org.air.repository.NationCodeRepository;
import org.air.repository.ScheduleRepository;
import org.air.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.meta.When;
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
import static org.junit.jupiter.api.Assertions.assertLinesMatch;


@Slf4j
@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private NationCodeRepository nationCodeRepository;


    private static List<Schedule> schedules = new ArrayList<>();
    private static User user;

    @BeforeAll
    public static void init() {

        user = User.builder()
                .userid("tester")
                .password("12345")
                .authority(
                        Authority.builder()
                                .authority("USER")
                                .build()
                )
                .build();
        Schedule schedule = Schedule.builder()
                .id(1L)
                .date("01May24")
                .cntto("CJU")
                .cntfrom("CJU")
                .user(user)
                .build();

        schedules.add(schedule);
    }

    @Test
    @DisplayName("select All Date")
    public void getAllSchedules() {
        Mockito.when(scheduleRepository.findByUserUserid("tester"))
                .thenReturn(schedules);


        List<Schedule> schedules = new ArrayList<>();
        String auth = user.getAuthority().getAuthority();


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

        List<NationCode> codes = nationCodeRepository.findAll();
        Map<String, Map<String, String>> codeCountryMap = convertToMap(codes);

        List<FlightData> list = generateFlightData(updatedSchedules, codeCountryMap);
        assertThat(auth, is("USER"));
        Assertions.assertNotNull(list);
    }

    @Test
    public void getTodaySchedules() {
        String today = "01May24";
        Mockito.when(scheduleRepository.findByUserAndDate(user, today))
                .thenReturn(schedules);


        List<Schedule> schedules = scheduleRepository.findByUserAndDate(user, "01May24");
        List<NationCode> codes = nationCodeRepository.findAll();
        assertThat(schedules.get(0).getCntto(), is("CJU"));

    }

    @Test
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

        List<NationCode> codes = nationCodeRepository.findAll();
        Map<String, Map<String, String>> code = convertToMap(codes);

        List<FlightData> list = generateFlightData(schedules, code);
        assertThat(list.get(0).getStab(), is("1810"));
    }


    // -------------------------- 일정 수정 삭제 저장 --------------------------

    public List<FlightData> generateFlightData(List<Schedule> sList, Map<String, Map<String, String>> codes) {
        List<FlightData> flightDataList = new ArrayList<>();
        for (Schedule s : sList) {
            FlightData flightData = FlightData.builder()
                    .departureShort(s.getCntfrom())
                    .departure(codes.getOrDefault(s.getCntfrom(), Collections.emptyMap()).getOrDefault("code", "Unknown"))
                    .date(s.getDate())
                    .destinationShort(s.getCntto())
                    .destination(codes.getOrDefault(s.getCntto(), Collections.emptyMap()).getOrDefault("code", "Unknown"))
                    .flightNumber(s.getPairing())
                    .stal(s.getStal())
                    .stab(s.getStab())
                    .stdl(s.getStdl())
                    .stdb(s.getStdb())
                    .activity(s.getActivity())
                    .id(s.getId())
                    .ci(s.getCi())
                    .co(s.getCo())
                    .lat(codes.getOrDefault(s.getCntto(), Collections.emptyMap()).getOrDefault("lat", "unknown"))
                    .lon(codes.getOrDefault(s.getCntto(), Collections.emptyMap()).getOrDefault("lon", "unknown"))
                    .build();
            flightDataList.add(flightData);
        }

        return flightDataList;
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