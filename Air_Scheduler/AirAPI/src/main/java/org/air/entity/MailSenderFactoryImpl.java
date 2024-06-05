package org.air.entity;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailSenderFactoryImpl implements MailSenderFactory {

   @Override
   public JavaMailSender getSender(final String email, final String password) {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("smtp.gmail.com");
      mailSender.setUsername(email);
      mailSender.setPassword(password);
      mailSender.setPort(587);

      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");
      props.put("mail.mime.charset", "UTF-8");

      return mailSender;
   }
}