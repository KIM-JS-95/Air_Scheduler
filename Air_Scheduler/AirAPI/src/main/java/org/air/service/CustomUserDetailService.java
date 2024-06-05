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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User loadUserByUser(UserDTO login_user) {
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

    @Transactional
    public User loadUserByToken(String userid) {
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid) : null;
        return user;
    }


    // 기존 함수를 새 함수로 대체
    public Temppilotcode save_pilotcode(TemppilotcodeDAO temppilotcodeDTO) {
        return processPilotcode(temppilotcodeDTO);
    }

    public boolean delete_temp_pilotcode(TemppilotcodeDAO temppilotcodeDTO) {
        return temppilotcodeRepository.deleteByEmail(temppilotcodeDTO.getEmail());
    }

    public Temppilotcode login_check(TemppilotcodeDAO temppilotcodeDTO) {
        return processPilotcode(temppilotcodeDTO);
    }


    public Temppilotcode processPilotcode(TemppilotcodeDAO temppilotcodeDTO) {
        String randomKey = generateRandomKey(5);
        String[] userid = temppilotcodeDTO.getEmail().split("@");
        Temppilotcode temppilotcode = Temppilotcode.builder()
                .phonenumber(temppilotcodeDTO.getPhonenumber())
                .email(temppilotcodeDTO.getEmail())
                .userid(userid[0])
                .randomkey(randomKey)
                .username(temppilotcodeDTO.getUsername())
                .build();

        return temppilotcodeRepository.save(temppilotcode);
    }

    public int savePilot(UserDTO user) {
        Temppilotcode temppilotcode = temppilotcodeRepository.findByRandomkey(user.getRandomkey());
        if (temppilotcode == null) return 0; // 임시 가입이 되어 있지 않음
        User newUser;

        Authority pilotAuthority = Authority.builder()
                .id(1L)
                .authority("USER")
                .build();

        String[] userid = temppilotcode.getEmail().split("@");
        newUser = User.builder()
                .userid(userid[0])
                .password(user.getPassword()) // *
                .email(temppilotcode.getEmail())
                .name(temppilotcode.getUsername())
                .androidid(user.getAndroidid())
                .authority(pilotAuthority)
                .build();
        userRepository.save(newUser); // 최종 저장

        temppilotcodeRepository.deleteById(temppilotcode.getId()); // 저장이 되었으면 삭제시켜주기
        return 1; // 성공
    }

    public int saveFamily(UserDTO user) { // family_id ,userid, password, device_token, androidid
        User pilot = userRepository.findByUserid(user.getFamily_id()); // 기장 존재 확인

        if (pilot != null) {
            Authority familyAuthority = Authority.builder()
                    .id(2L)
                    .authority("FAMILY")
                    .build();

            User family = User.builder()
                    .userid(user.getUserid())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .name(user.getName())
                    .family(user.getFamily_id())
                    .androidid(user.getAndroidid())
                    .authority(familyAuthority) // 권한
                    .build();
            try {
                userRepository.save(family);
            } catch (Exception e) {
                return 3;
            }
        } else {
            return 0; // 파일럿 계정이 없을 경우
        }
        return 1; // 성공
    }

    public int getSchedule_chk(String userid) {
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
            user.setEmail(user_modify.getEmail());
            user.setPassword(user_modify.getPassword());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean certification_devide(String userid, String androidid) {
        if (temppilotcodeRepository.existsByUserid(userid)) {
            // 파일럿 코드가 존재한다면 -> 유저 테이블 androidId 변경해주고 -> 임시 테이블 지워주고
            User user = userRepository.findByUserid(userid);
            user.setAndroidid(androidid);
            userRepository.save(user); // 변경사항 저장
        }
        int deletedCount = temppilotcodeRepository.deleteByUserid(userid);
        return deletedCount > 0;
    }

    public String generateRandomKey(int length) {
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

    public boolean exist_userid(String userid){
        return userRepository.existsByUserid(userid);
    }


}