package com.example.schoolmuapp.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("");     //get from email
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }
    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public String passwordResetTokenMail(User user, String applicationUrl, String two2Fatoken) {
        String url = applicationUrl + "/user/savePassword?token=" + two2Fatoken;

        //gmail java email send url to user email to reset password/////////////////////////////////
        this.sendSimpleEmail(
                user.getUsername(),
                "Click on your Password link to reset your Password: " + url,
                "Password Reset Link Sent");

        //log url in console to see what was sent to user email
        log.info("Click link to reset your password: {}", url);
        return url;
    }

}
