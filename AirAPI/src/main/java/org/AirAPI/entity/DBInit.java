package org.AirAPI.entity;

import org.AirAPI.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DBInit implements CommandLineRunner {

    @Autowired
    private CustomUserDetailService userService;

    @Override
    public void run(String... args) throws Exception {

        User user1 = User.builder()
                .name("침착맨")
                .userId("001200")
                .enabled(true)
                .authorities(Set.of(Authority.USER))
                .build();
        userService.save(user1);
    }
}