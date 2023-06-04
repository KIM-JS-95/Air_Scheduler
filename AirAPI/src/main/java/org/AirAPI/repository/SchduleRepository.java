package org.AirAPI.repository;

import org.AirAPI.entity.Schedule;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findById(int i);

}
