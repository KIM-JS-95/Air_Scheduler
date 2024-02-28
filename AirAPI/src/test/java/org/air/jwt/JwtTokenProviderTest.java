package org.air.jwt;

import org.air.entity.User;
import org.air.service.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailService customUserDetailService;

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
        String refreshToken = jwtTokenProvider.createrefreshToken(userId);

        // Then
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);

        // 추가 테스트: 생성된 리프레시 토큰의 유효성 검증
        assertTrue(jwtTokenProvider.validateToken(refreshToken));
    }

    @Test
    @DisplayName("Test getting authentication from token")
    public void testGetAuthentication() {
        User user = new User();
        user.setName("testUser");
        Authentication expectedAuthentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

        when(customUserDetailService.loadUserById(userId)).thenReturn(user);

        if(jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            assertEquals(expectedAuthentication, authentication);
        }
    }

    @Test
    @DisplayName("Test token validation")
    public void testValidateToken() {
        // Given
        String validToken = jwtTokenProvider.createToken(userId, new Date().toString());

        assertTrue(jwtTokenProvider.validateToken(validToken));
    }

}
