package org.air.entity;

import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.air.service.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {JwtTokenProvider.class})
class UserTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private static User user;
    private static String token;

    @BeforeEach
    public void init() {
        user = User.builder()
                .userid("001200")
                .name("tester")
                .password("1234")
                .build();

        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(new Date()));
    }

    @Test
    public void user_set() {

        Authority authority = Authority.builder()
                .authority("ROLE_USER")
                .build();

        User user = User.builder()
                .userid("001200")
                .name("tester")
                .password("1234")
                .email("123@gmail.com")
                .authority(authority)
                .build();

        assertThat(user.getAuthority().getAuthority(),
                Matchers.is(authority.getAuthority()));
    }

    @Test
    @DisplayName("login and set token")
    public void login_user() {
        Refresh refresh = Refresh.builder()
                .token(token)
                .build();
        user.setRefresh(refresh);

        assertThat(user.getRefresh().getToken(), Matchers.is(token));
    }

}