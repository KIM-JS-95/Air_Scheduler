package org.air.service;

import org.air.entity.Authority;
import org.air.entity.RefreshToken;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.air.entity.Authority.ROLE_USER;

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
        User user = userRepository.existsByUserid() ? userRepository.findByUserid(userid): null;
        return user;
    }

    // user save
    public User save(User user) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(ROLE_USER));
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