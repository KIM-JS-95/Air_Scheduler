package org.air.service;

import org.air.entity.RefreshToken;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


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