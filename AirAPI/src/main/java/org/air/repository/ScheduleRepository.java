package org.air.repository;

import org.air.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 수정
    Schedule findById(int i);

    // 일정 저장
    // 3일치 일정 획득
    List<Schedule> findByDateBetween(String startDate,String endDate);

    boolean deleteById(int i);

    boolean existsByDate(String sDate);
}
