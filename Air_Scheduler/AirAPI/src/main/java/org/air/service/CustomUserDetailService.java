package org.air.service;

import lombok.extern.slf4j.Slf4j;
import org.air.entity.*;
import org.air.repository.AuthorityRepository;
import org.air.repository.TemppilotcodeRepository;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;


@Slf4j
@Service
public class CustomUserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private TemppilotcodeRepository temppilotcodeRepository;


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


    public Temppilotcode save_pilotcode(TemppilotcodeDAO temppilotcodeDTO){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPilotcode = passwordEncoder.encode(temppilotcodeDTO.getPilotcode());

        String randomKey = generateRandomKey(5);
        Temppilotcode temppilotcode = Temppilotcode.builder()
                .pilotcode(hashedPilotcode)
                .phonenumber(temppilotcodeDTO.getPhonenumber())
                .email(temppilotcodeDTO.getEmail())
                .randomkey(randomKey)
                .build();

        return temppilotcodeRepository.save(temppilotcode);
    }

    private static String generateRandomKey(int length) {
        // 무작위 문자열을 구성할 문자들
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        // 길이만큼 무작위 문자 선택하여 문자열 생성
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    public int savePilot(UserDTO user) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPilotcode = passwordEncoder.encode(user.getPilotcode());

            Authority pilotAuthority = Authority.builder()
                    .id(1L)
                    .authority("USER")
                    .build();

            if (temppilotcodeRepository.existsByRandomkeyAndPilotcode(user.getUserid(), hashedPilotcode)) {
                User newUser = User.builder()
                        .pilotcode(user.getPilotcode())
                        .userid(user.getUserid())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .name(user.getName())
                        .authority(pilotAuthority)
                        .build();
                userRepository.save(newUser);
            } else {
                return 0; // 파일럿 계정이 없을 경우
            }
        } catch (Exception e) {
            return 3; // 오류 발생
        }
        return 1; // 성공
    }

    public int saveFamily(UserDTO user) {
        try {
            Authority familyAuthority = Authority.builder()
                    .id(2L)
                    .authority("FAMILY")
                    .build();

            if (temppilotcodeRepository.existsByPilotcode(user.getPilotcode())) {
                User newUser = User.builder()
                        .pilotcode(user.getPilotcode() + user.getUserid()) // 가족은 파일럿코드 + 아이디
                        .userid(user.getUserid())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .name(user.getName())
                        .family(user.getFamily())
                        .authority(familyAuthority)
                        .build();
                userRepository.save(newUser);
            } else {
                return 0; // 파일럿 계정이 없을 경우
            }
        } catch (Exception e) {
            return 3; // 오류 발생
        }
        return 1; // 성공
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