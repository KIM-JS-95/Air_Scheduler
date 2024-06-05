package org.air.service;

import org.air.entity.MailSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String usernameid;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.service_domain}")
    private String service_domain; // http:// ~~

    private final MailSenderFactory mailSenderFactory;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(MailSenderFactory mailSenderFactory, TemplateEngine templateEngine) {
        this.mailSenderFactory = mailSenderFactory;
        this.templateEngine = templateEngine;
    }

    public boolean sendTokenMail(String username, String email, String randomkey) {
        try {
            Context context = new Context();
            context.setVariable("name", username);
            context.setVariable("message", randomkey);
            JavaMailSender emailSender = mailSenderFactory.getSender(usernameid, password);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String htmlContent = templateEngine.process("emailTemplate", context);

            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(usernameid);
            helper.setFrom(internetAddress);

            internetAddress.setAddress(email);
            helper.setTo(internetAddress);

            helper.setSubject("[Schedule App] 회원가입을 위한 랜덤키를 전달해 드립니다.");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean sendLoginCautionMail(String username, String email, String userid, String androidid) throws MessagingException, IOException {

        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("link", service_domain + "?userid=" + userid + "&androidid=" + androidid);
        System.out.println(service_domain + "?userid=" + userid + "&androidid=" + androidid);
        JavaMailSender emailSender = mailSenderFactory.getSender(usernameid, password);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String htmlContent = templateEngine.process("emailTemplate2", context);

        helper.setFrom(usernameid);
        helper.setTo(email);
        helper.setSubject("[Schedule App] 새로운 기기에서 접근이 감지되었습니다.");
        helper.setText(htmlContent, true);

        try {
            emailSender.send(message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
