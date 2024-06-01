package org.air.repository;

import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUseridAndPassword(String userid, String password);
    boolean existsByUseridAndPassword(String userid, String password);
    List<User> findAll();

    List<User> findByName(String username);
    boolean existsByUserid(String userid);

    User findByUserid(String userid);
    User findByPilotcode(String pilotcode);
    List<User> findByFamily(String pilotcode);

    List<User> findByAuthority(Authority authority);
}