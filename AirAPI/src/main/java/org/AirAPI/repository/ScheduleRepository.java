package org.AirAPI.repository;

import org.AirAPI.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findById(int i);
    List<Schedule> saveAll(List<Schedule> schedules);

}
