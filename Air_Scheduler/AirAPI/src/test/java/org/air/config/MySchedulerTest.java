package org.air.config;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MySchedulerTest {

    @Test
    public void Month() {
        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonthDate = currentDate.minusMonths(1);
        String lastMonth = lastMonthDate.format(DateTimeFormatter.ofPattern("MM"));
        System.out.println("Last month: " + lastMonth);

        // 1월부터 12월까지 월의 약자를 리스트에 추가
        Month month = Month.of(Integer.parseInt(lastMonth));
        String abbreviation = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        // 변수 출력
        System.out.println("월의 약자 리스트:" + abbreviation);
    }

}