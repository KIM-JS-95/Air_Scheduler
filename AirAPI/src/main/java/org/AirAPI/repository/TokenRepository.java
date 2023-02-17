package org.AirAPI.repository;

import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUsername(String name);
}
