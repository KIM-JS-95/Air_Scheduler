package org.air.service;

import org.air.config.AWStextrack;
import org.air.entity.json.Jsonschedules;
import org.air.jwt.JwtTokenProvider;
import org.air.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;


@DataJpaTest
@Import(ScheduleService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Datatest {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void delete_test(){
        scheduleRepository.truncateTable();
    }

}
