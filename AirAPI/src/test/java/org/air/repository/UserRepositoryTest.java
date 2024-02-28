package org.air.repository;

import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;


@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser
    public void findByUserid(){
        User user =userRepository.findByUserid("001200");
        assertThat(user.getUserid(), is("001200"));
        assertThat(user.getAuthorities().get(0), is(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @WithMockUser
    public void findAll(){
        List<User> user =userRepository.findAll();
        assertThat(user.get(0).getUserid(), is("001200"));

    }

    @Test
    @WithMockUser
    public void findByName(){
        List<User> user =userRepository.findByName("침착맨");
        assertThat(user, not(empty()));

    }
}