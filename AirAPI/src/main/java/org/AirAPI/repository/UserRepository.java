package org.AirAPI.repository;

import org.AirAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserId(String userid);
    User findByUserName(String username);
}
