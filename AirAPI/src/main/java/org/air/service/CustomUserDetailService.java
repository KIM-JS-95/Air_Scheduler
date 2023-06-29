package org.air.service;

import org.air.entity.RefreshToken;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD:AirAPI/src/main/java/org/air/service/CustomUserDetailService.java
import org.springframework.stereotype.Service;

import java.util.Optional;
=======
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
>>>>>>> a4ef8f5c2380d18fb7ab077ea73d99fd23deeca7:AirAPI/src/main/java/org/AirAPI/service/CustomUserDetailService.java


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
        User user = userRepository.findByUserid(userid).orElseGet(User::new);
        return user;
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