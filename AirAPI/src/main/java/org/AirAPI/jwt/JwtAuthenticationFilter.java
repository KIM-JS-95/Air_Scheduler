//package org.AirAPI.jwt;
//
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.AirAPI.entity.Messege;
//import org.AirAPI.entity.RefreshToken;
//import org.AirAPI.entity.StatusEnum;
//import org.AirAPI.repository.TokenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Optional;
//
//
////해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달합니다.
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends GenericFilterBean {
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    //@SneakyThrows
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
//
//        // 토큰이 유효하다면
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰으로부터 유저 정보를 받아
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            // SecurityContext 에 객체 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        // 다음 Filter 실행
//        chain.doFilter(request, response);
//    }
//        /*
//           메인 토큰 검증 -> O ) Context에 저장 후 필터 탈출
//                      -> 리프레시 토큰 검증 -> X ) 리프레시 토큰 재발급 후 필터 탈출
//         */
//        /*
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//
//            // 메인 토큰은 존재하는데 리프레시 토큰이 없다면?
//            Optional<RefreshToken> refreshToken = tokenRepository.findByUsername(jwtTokenProvider.getUserPk(token));
//            if(refreshToken.isEmpty()) {
//                throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
//            }
//            Date ex_date =  jwtTokenProvider.getDate(refreshToken.get().getToken());
//
//
//            // 기간 만료일 경우 refresh token 새로 발급할 것
//            if(ex_date.compareTo(new Date())<0){
//                jwtTokenProvider.refreshToken(jwtTokenProvider.getUserPk(token));
//                tokenRepository.save(RefreshToken.builder()
//                        .username(jwtTokenProvider.getUserPk(token))
//                        .token(refreshToken.get().getToken())
//                        .build());
//            }
//
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        chain.doFilter(request, response);
//        */
//    }