package org.AirAPI.entity;

import org.air.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;

class UserTest {

    @Test
    public void user_set(){
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User user = User.builder()
                .userid("001200")
                .name("kim")
                .email("123@gmail.com")
                .build();
        assertThat(user.getAuthorities(), Matchers.is(authorities));
    }
}