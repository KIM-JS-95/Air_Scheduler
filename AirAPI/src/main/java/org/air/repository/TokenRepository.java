package org.air.repository;

import org.air.entity.Refresh;
import org.air.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Refresh, Long> {

    Refresh findByToken(String token);
}
