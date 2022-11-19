package org.AirAPI.Repository;


import org.AirAPI.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchduleRepository extends JpaRepository<Schedule, Integer> {

}
