package org.air.repository;

import org.air.entity.Temppilotcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemppilotcodeRepository extends JpaRepository<Temppilotcode, Long> {

    boolean existsByRandomkeyAndPilotcode(String randomkey, String pilotcode);

    boolean existsByPilotcode(String randomkey);
}
