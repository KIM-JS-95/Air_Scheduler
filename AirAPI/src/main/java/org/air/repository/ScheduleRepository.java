package org.air.repository;

import org.air.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 수정
    Schedule findById(int i);
    // 일정 저장
    // 3일치 일정 획득
    @Query("SELECT e FROM schedule e WHERE e.date >= startDate limit 3")
    List<Schedule> getScheduleTreedays(String startDate);

    boolean deleteById(int i);
}
