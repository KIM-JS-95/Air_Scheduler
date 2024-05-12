package org.air.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.air.entity.User;
import org.air.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JwtTokenProvider {
    private String secretKey = "myprojectsecret";

    // 토큰 유효시간 30분
    private long tokenValidTime = 60 * 60 * 1000L;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userid, String access_time) {

        Claims claims = Jwts.claims().setSubject(userid); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("userid", userid);
        claims.put("access_time", access_time);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                .compact();
    }

    public String createrefreshToken(String userid, String access_time){
        Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("userid", userid);
        claims.put("access_time", access_time);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(new Date()) // 토큰 발행 시간 정보
                .setExpiration(new Date(new Date().getTime() + tokenValidTime+10)) // set Expire Time 30분 + 10분
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    @Transactional
    public Authentication getAuthentication(String token) {

        User user = customUserDetailService.loadUserByToken(this.getUserPk(token));
        String authority = user.getAuthority().getAuthority();

        List<String> allowedRoles = Arrays.asList("USER", "ADMIN");
        if (!allowedRoles.contains(authority.toUpperCase())) {
            throw new SecurityException("Access Denied: User does not have the required authority");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authority));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }


    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return null; // 또는 다른 적절한 값을 반환
        }
    }

    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();

            long remainingValidity = expirationDate.getTime() - currentDate.getTime();
            if (remainingValidity <= 0) {
                // 토큰이 만료됨
                return false;
            } else {
                // 토큰이 만료되지 않았지만, 남은 유효 기간이 3분 이하일 경우
                long threshold = 3 * 60 * 1000; // 3분을 밀리초로 변환
                return remainingValidity <= threshold;
            }
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생
            e.printStackTrace();
            return false;
        }
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            // 만료되었을 경우 예외 처리됨
            return false;
        }
    }


    public boolean Tokencheck(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            // 파싱중 오류발행
            return false;
        }
    }


}