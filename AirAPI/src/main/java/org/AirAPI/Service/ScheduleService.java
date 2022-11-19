package org.AirAPI.Service;

import org.AirAPI.Entity.Schedule;
import org.AirAPI.Repository.SchduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    SchduleRepository schduleRepository;

    public Schedule findData(int id){
        return schduleRepository.findById(id).orElseThrow();

    }
}
