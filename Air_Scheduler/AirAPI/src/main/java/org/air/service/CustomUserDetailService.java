package org.air.service;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class CustomUserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public User loadUserByUser(User login_user) {
        User user = userRepository.existsByUseridAndPassword(login_user.getUserid(), login_user.getPassword())
                ? userRepository.findByUseridAndPassword(login_user.getUserid(), login_user.getPassword()) : null;

        user.setDevice_token(login_user.getDevice_token());
        return user;
    }

    @Transactional
    public User loadUserByToken(String userid) {
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid) : null;
        return user;
    }

    public boolean save(User user) {
        try {
            Authority authority = Authority.builder()
                    .authority("ROLE_USER")
                    .build();
            user.setAuthority(authority);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // token save
    @Transactional
    public boolean token_save(User user, String token) {
            Refresh refreshToken = Refresh.builder()
                    .id(0)
                    .token(token)
                    .build();
            tokenRepository.save(refreshToken);

            User user1 = userRepository.findByUserid(user.getUserid());
            user1.getRefresh().setToken(token);
            return true;
    }

    @Transactional
    public boolean modify(User user_modify, String token) {
        try {
            // 1. 유저 확인
            User user = userRepository.existsByUserid(token) ? userRepository.findByUserid(token) : null;
            user.setUserid(user_modify.getUserid());
            user.setEmail(user_modify.getEmail());
            user.setPassword(user_modify.getPassword());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // findByToken
    public boolean logout(String userid) {
        return true;
    }

}