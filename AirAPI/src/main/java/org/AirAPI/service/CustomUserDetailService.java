package org.AirAPI.service;

import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class CustomUserDetailService{

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public User loadUserByUsername(String username){
        return userRepository.findByName(username);
    }

    public User loadUserById(String userid){
        return userRepository.findByUserId(userid);
    }

    // user save
    public User save(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        userRepository.save(user);
        return user;
    }
    // token save
    public RefreshToken token_save(String refreshtoken) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userid("침착맨")
                .token(refreshtoken)
                .build();
        return tokenRepository.save(refreshToken);
    }


}