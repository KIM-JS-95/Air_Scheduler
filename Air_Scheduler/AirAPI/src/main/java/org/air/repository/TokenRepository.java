package org.air.repository;

import org.air.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Refresh, Integer> {

    Refresh findByToken(String token);
     void deleteById(Integer id);


}
