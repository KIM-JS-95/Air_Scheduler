package org.air.repository;

import org.air.entity.Schedule;
import org.air.entity.User;
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

    // 일정 저장
    @Query("SELECT s FROM Schedule s WHERE s.date >= :startDate AND s.date <= :endDate")
    List<Schedule> findByDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    void deleteById(Long i);

    List<Schedule> findByDate(String date);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE schedule", nativeQuery = true)
    void truncateTable();

    @Query("SELECT s FROM Schedule s WHERE s.id >= :startId AND s.id <= :endId")
    List<Schedule> findByIdBetween(@Param("startId")Long startId, @Param("endId") Long endId);

    void deleteAllByUserPilotcode(String pilotcode);


    List<Schedule> findByUserPilotcode(String pilotcode);

    @Query("SELECT s FROM Schedule s WHERE s.user = :user AND s.date = :date")
    List<Schedule> findByUserAndDate(@Param("user") User user, @Param("date") String date);

}
