package org.air.service;

import org.air.entity.Temppilotcode;
import org.air.entity.TemppilotcodeDAO;
import org.air.entity.User;
import org.air.repository.TokenRepository;
import org.air.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import java.util.Random;

import static org.junit.Assert.assertEquals;


@SpringBootTest
class CustomUserDetailServiceTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Test
    public void emailtoid(){
        String email = "test123@gmail.com";
        String[] email_array = email.split("@");
    }

    @Test
    @WithMockUser
    @Transactional
    void loadUserByToken() {
        // 호출
        String userid= "001200";
        User user = userRepository.existsByUserid(userid) ? userRepository.findByUserid(userid) : null;
        System.out.println(user.getAuthority().toString());
    }

    @Test
    @WithMockUser
    void logout() {
        // 호출
         User result_user = userRepository.findByUseridAndPassword("123", "123");
        result_user.setRefresh(null);
        userRepository.save(result_user);

    }


    @Test
    public void testSavePilotcode() {
        // given
        TemppilotcodeDAO temppilotcodeDTO = TemppilotcodeDAO.builder()
                .phonenumber("1234567890")
                .email("test@example.com")
                .build();


        // when
        Temppilotcode savedPilotcode = customUserDetailService.save_pilotcode(temppilotcodeDTO);

        assertEquals("1234567890", savedPilotcode.getPhonenumber());
        assertEquals("test@example.com", savedPilotcode.getEmail());
    }

    @Test
    public void randomKey () {
        String randomKey = generateRandomKey(5);
        System.out.println("Random Key: " + randomKey);
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
}