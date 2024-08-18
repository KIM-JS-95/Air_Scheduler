package org.air.repository;

import org.air.entity.NationCode;
import org.air.entity.Schedule;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

// @Springboot -> @DataJpaTest 으로 경량화 진행
@SpringBootTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @Disabled
    public void save_test() {

        NationCode nationCode_cntFrom = NationCode.builder()
                .country("제주")
                .code("CJU")
                .lat("0.5")
                .lon("0.5")
                .build();

        NationCode nationCode_cntTo = NationCode.builder()
                .country("김포")
                .code("GMP")
                .lat("0.1")
                .lon("0.1")
                .build();

        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .stdl("0000") // 출발 시간
                .stal("2359") // 도착 시간
                .cntFrom(nationCode_cntFrom) // 출발
                .cntTo(nationCode_cntTo) // 도착
                .activity("OFF")
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .date("01Nov23")
                .stdl("0000") // 출발 시간
                .stal("2359") // 도착 시간
                .cntFrom(nationCode_cntFrom) // 출발
                .cntTo(nationCode_cntTo) // 도착
                .activity("OFF")
                .build();

        List<Schedule> schedule_list = new ArrayList<>();
        schedule_list.add(schedule1);
        schedule_list.add(schedule2);
        scheduleRepository.saveAll(schedule_list);

    }

    @Test
    public void call_schedule(){
        String startdateString = "01Nov23";
        String enddateString = "03Nov23";
        List<Schedule> schedules = scheduleRepository.findByDateBetween(startdateString, enddateString);
        assertThat(schedules.get(1).getDate(),is("03Nov23"));

        schedules.stream().forEach(element -> {
            // 요소에 대한 작업 수행
            System.out.println(element);
        });
    }

    @Test
    public void call_schedule_by_id(){
        Long id = 8L;
        Optional<Schedule> schedules = scheduleRepository.findById(id);
        assertThat(schedules, is(notNullValue()));

    }

    @Test
    @DisplayName("find All schedule")
    public void scheduleAll(){
        List<Schedule> schedules = scheduleRepository.findAll();

        schedules.stream().forEach(element -> {
            // 요소에 대한 작업 수행
            System.out.println(element);
        });
    }

    @Test
    @Transactional
    public void notice_next_day_schedules() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        // 현재 날짜 가져오기
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); // 1일 추가

        // 날짜를 포맷팅하여 문자열로 변환
        String startDate = dateFormat.format(calendar.getTime());

        // 사용자 ID 목록 생성
        List<String> userIds = new ArrayList<>();
        userIds.add("test");

        // 쿼리 메서드 호출
        List<Schedule> schedules = scheduleRepository.findByUseridAndDate(userIds, startDate);
        for(Schedule schedule : schedules){
            System.out.println(schedule);
        }
    }
}