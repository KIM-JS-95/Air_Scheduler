package org.air.entity;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailSenderFactory {
    JavaMailSender getSender(String email, String password);
}