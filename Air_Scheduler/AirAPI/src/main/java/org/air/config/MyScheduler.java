package org.air.config;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.air.service.FcmServiceImpl;
import org.air.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyScheduler {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private FcmServiceImpl fcmService;

    @Scheduled(cron = "0 0 0 5 * ?") // 매달 5일 자정에 실행
    public void delete_cron_set() {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM"); // "MMM" 형식으로 변경
        String month = dateFormat.format(today); // 현재 월을 "Jul", "Aug" 등의 형식으로 얻기

        scheduleService.delete_cron(month);
    }

    @Scheduled(cron = "0 0 14 25 * ?") // 매달 25일 오후 14시에 실행
    public void schedule_cron_set() throws FirebaseMessagingException {
        fcmService.request_schedule_save();
    }
}