package ru.asmisloff.studyplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendCredentials(String email, String login, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("a.smisloff@gmail.com");
        mailMessage.setText("adswewe");
        mailMessage.setSubject("asdasffas");
        mailMessage.setFrom("a.smisloff@yandex.ru");
        mailSender.send(mailMessage);
    }
}
