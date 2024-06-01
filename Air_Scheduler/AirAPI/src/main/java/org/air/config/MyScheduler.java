package org.air.config;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.air.service.FcmServiceImpl;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class MyScheduler {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private FcmServiceImpl fcmService;

    @Scheduled(cron = "0 0 0 5 * ?") // 매달 5일 자정에 실행
    public void delete_cron_set() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonthDate = currentDate.minusMonths(1);
        String lastMonth = lastMonthDate.format(DateTimeFormatter.ofPattern("MM"));
        Month month = Month.of(Integer.parseInt(lastMonth));
        String abbreviation = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        scheduleService.delete_cron(abbreviation);
    }

    @Scheduled(cron = "0 0 14 25 * ?") // 매달 25일 오후 14시에 실행
    public void schedule_cron_set() throws FirebaseMessagingException {
        fcmService.request_schedule_save();
    }
}