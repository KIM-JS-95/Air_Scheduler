package org.air.repository;

import org.air.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 수정
    Optional<Schedule> findById(Long id);

    // 일정 저장
    // 3일치 일정 획득
    @Query("SELECT s FROM Schedule s WHERE s.date >= :startDate AND s.date <= :endDate")
    List<Schedule> findByDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
    List<Schedule> findByIdBetween(Long startDate, Long endDate);
    void deleteById(Long i);

    Schedule findByDate(String date);

    boolean existsByDate(String sDate);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE schedule", nativeQuery = true)
    void truncateTable();
}
