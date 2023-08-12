package org.air.entity;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;


class ScheduleTest {
    @DisplayName("스케쥴_엔티티_테스트")
    @Test
    public void scheduleTest() {
        Schedule schedule = Schedule.builder()
                .id(1L)
                .date("01Nov22")
                .std("0000") // 출발 시간
                .sta("2359") // 도착 시간
                .cntFrom("GMP") // 출발
                .cntTo("GMP") // 도착
                .activity("OFF")
                .build();

        assertThat(schedule.getId(), Matchers.is(1));

    }

    @Test
    public void matcher() {
        String mattcher = "(Date|Pairing|DC|C/I|C/O|Activity|From|STD|To|STA|AC|Blk)";
        String a = "Date";
        if(a.matches(mattcher)) System.out.println(true);
        else System.out.println(false);

    }
}