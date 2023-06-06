package org.AirAPI.repository;

import org.AirAPI.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 수정
    Schedule findById(int i);
    // 일정 저장
    List<Schedule> saveAll(List<Schedule> schedules);
    // 3일치 일정 획득
    List<Schedule> getScheduleTreedays(int day);

    boolean deleteById(int i);
}
