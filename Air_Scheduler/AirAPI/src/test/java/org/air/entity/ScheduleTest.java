package org.air.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
class ScheduleTest {
    @DisplayName("스케쥴_엔티티_테스트")
    @Test
    public void scheduleTest() throws JsonProcessingException {

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

        Schedule schedule = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .stdl("0000") // 출발 시간
                .stdl("2359") // 도착 시간
                .cntFrom(nationCode_cntFrom) // 출발
                .cntTo(nationCode_cntTo) // 도착
                .activity("OFF")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        // Schedule 객체를 Pretty Print 형태의 JSON 문자열로 변환
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(schedule);

        System.out.println(prettyJson);
        assertThat(schedule.getCntTo().getCode(), Matchers.is("GMP"));

    }
}