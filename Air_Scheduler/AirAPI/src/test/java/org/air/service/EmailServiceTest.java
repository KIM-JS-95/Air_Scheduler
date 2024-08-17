package org.air.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;


    @Test
    public void testSendTokenMailSuccess() throws MessagingException, IOException {
        String username = "testuser";
        String email = "qsdsafd.com";
        String randomkey = "randomkey";

        boolean result = emailService.sendTokenMail(email,  randomkey);
        System.out.println(result);
    }

}