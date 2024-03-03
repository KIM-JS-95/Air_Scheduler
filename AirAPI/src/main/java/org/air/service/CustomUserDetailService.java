package org.air.service;

import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@Service
public class CustomUserDetailService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    public User loadUserById(String userid){
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid): null;
        return user;
    }

    // user sign up
    public User save(User user) {
        Authority authority = Authority.builder()
                .authority("ROLE_USER")
                .build();
        user.setAuthority(authority);
        userRepository.save(user);
        return user;
    }

    // token save
    public Refresh token_save(User user, String token) {

        Refresh refreshToken = Refresh.builder()
                .user(user)
                .token(token)
                .build();

        return tokenRepository.save(refreshToken);
    }

    // findByToken
    public boolean logout(HttpServletRequest request){
        try {
            Refresh refresh = tokenRepository.findByToken(request.getHeader("Authorization"));
            refresh.setToken("");
            refresh.getUser().setRefresh(null);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}