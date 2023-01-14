package org.AirAPI.repository;

import org.AirAPI.entity.Schedule;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Configuration
public interface SchduleRepository extends JpaRepository<Schedule, Integer> {

}
