package org.air.repository;

import org.air.entity.Temppilotcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemppilotcodeRepository extends JpaRepository<Temppilotcode, Long> {
    
    Temppilotcode findByRandomkey(String randomkey);

    boolean existsById(Long id);

    boolean deleteByEmail(String email);


    Long deleteByUserid(String userid);

    Temppilotcode findByUserid(String userid);

    boolean existsByUseridAndAndroidid(String userid, String androidid);

    Temppilotcode findTopByUseridOrderByCreatedAtDesc(String userid);
}
