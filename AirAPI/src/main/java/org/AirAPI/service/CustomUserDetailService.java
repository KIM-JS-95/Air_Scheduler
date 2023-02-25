package org.AirAPI.service;

import lombok.RequiredArgsConstructor;
import org.AirAPI.entity.User;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public User loadUserByUsername(String username){
        return userRepository.findByUserName(username);
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
    public void token_save(String refreshtoken) {
    }
}