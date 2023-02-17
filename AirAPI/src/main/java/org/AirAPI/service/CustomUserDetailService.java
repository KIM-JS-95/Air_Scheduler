package org.AirAPI.service;

import lombok.RequiredArgsConstructor;
import org.AirAPI.entity.RefreshToken;
import org.AirAPI.entity.User;
import org.AirAPI.repository.TokenRepository;
import org.AirAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

    //@Autowired
    //private  TokenRepository tokenRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserNickname(username);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByUserEmail(email);
    }

    public void save(User user) {
    }


    public void toeknsave(RefreshToken re_token) {
    }


}