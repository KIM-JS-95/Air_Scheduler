package org.air.jwt;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
@SpringBootTest
public class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenRepository tokenRepository;
    private final String userId = "testUser";
    private final String token = "exampleToken";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test token creation")
    public void testCreateToken() {
        // Given
        String accessTime = new Date().toString();

        // When
        String createdToken = jwtTokenProvider.createToken(userId, accessTime);

        // Then
        assertNotNull(createdToken);
        assertTrue(createdToken.length() > 0);

        // 추가 테스트: 생성된 토큰의 유효성 검증
        assertTrue(jwtTokenProvider.validateToken(createdToken));
    }

    @Test
    @DisplayName("Test refresh token creation")
    public void testCreateRefreshToken() {
        // When
        Date date = new Date();
        SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
        String refreshToken = jwtTokenProvider.createrefreshToken(userId, access_time.format(date),"1");

        // Then
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);

        // 추가 테스트: 생성된 리프레시 토큰의 유효성 검증
        assertTrue(jwtTokenProvider.validateToken(refreshToken));
    }


    @Test
    @DisplayName("Test token validation")
    public void testValidateToken() {
        // Given
        String validToken = jwtTokenProvider.createToken(userId, new Date().toString());

        assertTrue(jwtTokenProvider.validateToken(validToken));
    }


    @Test
    @Transactional
    @DisplayName("Token Check")
    public void Toke_refresh() {
        Authority authority = Authority.builder()
                .id(1L)
                .authority("USER")
                .build();

        User user = User.builder()
                .userid("001201")
                .name("tester")
                .email("test@123")
                .authority(authority)
                .build();

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMDEyMDEiLCJ1c2VyaWQiOiIwMDEyMDEiLCJhY2Nlc3NfdGltZSI6IjEwOjE0OjM4IiwiaWF0IjoxNzA5NDI4NDc4LCJleHAiOjE3MDk0MzAyNzh9.i_N1cBqyzXm960OufSRNsZF8QnejylwYAkVWDq7iGRM";

        if (jwtTokenProvider.Tokencheck(token)) { // 1. 토큰 형식체크
            String userid = jwtTokenProvider.getUserPk(token);

            if (jwtTokenProvider.validateToken(token)) { // 날짜 유효성 체크
                User user1 = userRepository.findByUserid(userid);
            } else { // refresh
                Date date = new Date();
                SimpleDateFormat access_time = new SimpleDateFormat("hh:mm:ss");
                log.info("Refresh your Token");
            }
        } else {
            log.info("토큰이 적합하지 않습니다.");
        }

    }

}
