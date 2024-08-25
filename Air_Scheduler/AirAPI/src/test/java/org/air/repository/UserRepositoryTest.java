package org.air.repository;

import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;


@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @WithMockUser
    public void findByUserid() {
        User user = userRepository.findByUseridAndPassword("123", "123");
        assertThat(user.getUserid(), is("123"));
    }

    @Test
    @WithMockUser
    @Transactional
    public void findAll() {
        List<User> user = userRepository.findAll();
        assertThat(user.get(0).getAuthority().getAuthority(), is("USER"));
    }

    @Test
    @WithMockUser
    public void findByName() {
        List<User> user = userRepository.findByName("tester");
        assertThat(user, not(empty()));
    }

    @Test
    @WithMockUser
    @Disabled
    public void signup() {
        // 권한 저장
        Authority authority = Authority.builder()
                .authority("USER")
                .build();
        authorityRepository.save(authority);

        List<User> users = new ArrayList<>();
        User user = User.builder()
                .name("tester")
                .userid("001201")
                .email("test@123")
                .authority(authority)
                .build();

        User result = userRepository.save(user);

        users.add(user);

        assertThat(result.getName(), is(user.getName()));
    }

    @Test
    @DisplayName("login")
    public void login() {
        User user = userRepository.findByUseridAndPassword("001201", "1234");
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        String token = jwtTokenProvider.createToken(user.getUserid(), access_time.format(new Date()));
        Refresh refresh = Refresh.builder()
                .token(token)
                .build();

        tokenRepository.save(refresh);
    }
}