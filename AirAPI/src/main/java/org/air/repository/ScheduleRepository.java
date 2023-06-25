package org.air.repository;

import org.air.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 수정
    Schedule findById(int i);
    // 일정 저장
    // 3일치 일정 획득

    boolean deleteById(int i);
}
