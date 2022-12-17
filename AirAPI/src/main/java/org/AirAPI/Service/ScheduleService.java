package org.AirAPI.Service;

import org.AirAPI.Entity.Schedule;
import org.AirAPI.Repository.SchduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private SchduleRepository schduleRepository;

    public Schedule findData(int id) {
        return schduleRepository.findById(id).orElseThrow();

    }
/*
    public String save(List<Schedule> schedules) {
        schduleRepository.saveAll(schedules);
        return "OK";
    }
*/

}
