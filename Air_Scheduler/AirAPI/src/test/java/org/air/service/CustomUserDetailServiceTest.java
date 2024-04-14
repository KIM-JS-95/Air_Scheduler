package org.air.service;

import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;


@SpringBootTest
class CustomUserDetailServiceTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    // Test user set up
    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("홍길동")
                .email("123")
                .password("123")
                .userid("123")
                .build();
        // Save user first without the refresh token
        userRepository.save(user);

        Refresh refresh = Refresh.builder()
                .token("1234")
                .build();
        tokenRepository.save(refresh); // Save the refresh token to generate an ID

        user.setRefresh(refresh);
        userRepository.save(user); // Save the user again to update with the refresh token
    }



    @Test
    @WithMockUser
    @Transactional
    void loadUserByToken() {
        // 호출
        String userid= "001200";
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid) : null;
        System.out.println(user.getAuthority().toString());
    }

    @Test
    @WithMockUser
    void logout() {
        // 호출
         User result_user = userRepository.findByUseridAndPassword("123", "123");
        result_user.setRefresh(null);
        userRepository.save(result_user);

    }
}