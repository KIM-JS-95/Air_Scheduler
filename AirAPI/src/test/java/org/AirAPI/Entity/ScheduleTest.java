package org.AirAPI.Entity;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
class ScheduleTest {
    @Test
    public void entity1(){
        Schedule schedule = Schedule.builder()
                .id(1)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .from("GMP") // 출발
                .to("GMP") // 도착
                .activity("OFF")
                .build();

        assertThat(schedule.getId(), is(1));

    }


}