package org.air.service;

import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomUserDetailService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public User loadUserByUsername(String username){
        return userRepository.findByName(username);
    }

    public User loadUserById(String userid){
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid): null;
        return user;
    }

    // user save
    public User save(User user) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        userRepository.save(user);
        return user;
    }
    // token save
    public Refresh token_save(String refreshtoken, String userid) {
        Refresh refreshToken = Refresh.builder()
                .userid(userid)
                .token(refreshtoken)
                .build();
        return tokenRepository.save(refreshToken);
    }

}