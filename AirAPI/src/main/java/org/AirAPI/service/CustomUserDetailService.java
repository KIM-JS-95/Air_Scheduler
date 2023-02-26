package org.AirAPI.service;

import lombok.RequiredArgsConstructor;
import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@Configuration
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public User loadUserByUsername(String username){
        return userRepository.findByName(username);
    }

    public User loadUserById(String userid){
        return userRepository.findByUserId(userid);
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