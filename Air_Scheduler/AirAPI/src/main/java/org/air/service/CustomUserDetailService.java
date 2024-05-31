package org.air.service;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.Authority;
import org.air.entity.Refresh;
import org.air.entity.User;
import org.air.repository.AuthorityRepository;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class CustomUserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public User loadUserByUser(User login_user) {
        // 사용자가 존재하는지 확인
        boolean userExists = userRepository.existsByUseridAndPassword(login_user.getUserid(), login_user.getPassword());
        if (userExists) {
            // 사용자가 존재하면 정보를 가져옴
            User user = userRepository.findByUseridAndPassword(login_user.getUserid(), login_user.getPassword());
            // 가져온 사용자 객체에 기기 토큰 설정
            user.setDevice_token(login_user.getDevice_token());
            return user;
        } else {
            // 사용자가 존재하지 않으면 null 반환
            return null;
        }
    }


    @Transactional
    public User loadUserByToken(String userid) {
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid) : null;
        return user;
    }

    public boolean save(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int getSchedule_chk(String userid){
        User user = userRepository.findByUserid(userid);
        int check = user.getSchedule_chk();
        return check;
    }
    @Transactional
    public boolean set_schedule_status(String userid, int month) {
        try {
            User user = userRepository.findByUserid(userid);
            user.setSchedule_chk(month);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // token save
    @Transactional
    public boolean token_save(User user, String token) {
        User member = userRepository.findByUserid(user.getUserid());
        if (member == null) {
            throw new IllegalArgumentException("User not found");
        }
        Refresh refreshToken = member.getRefresh();
        if (refreshToken == null) { // 토큰이 없으면
            refreshToken = Refresh.builder()
                    .token(token)
                    .build();
            refreshToken = tokenRepository.save(refreshToken); // Refresh 엔티티를 먼저 저장
            member.setRefresh(refreshToken);
        } else { // 토큰이 있다면
            refreshToken.setToken(token);
        }
        userRepository.save(member); // User 엔티티를 저장하여 외래 키 관계를 업데이트
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
        } catch (Exception e) {
            return false;
        }
    }

    // findByToken
    @Transactional
    public boolean logout(String user_string) {
        User user = userRepository.findByUserid(user_string);
        System.out.println(user.toString());
        Integer token_id = user.getRefresh().getId();
        user.setRefresh(null);
        tokenRepository.deleteById(token_id);
        return true;
    }

}