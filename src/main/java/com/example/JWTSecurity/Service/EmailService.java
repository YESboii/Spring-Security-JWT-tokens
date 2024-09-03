package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.exceptionHandling.exceptions.EmailServiceException;
import jakarta.annotation.PostConstruct;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class EmailService {
    @Value("${app.email}")
    private   String email;
    @Value("${app.password}")
    private  String password;
    private JavaMailSenderImpl mailSender;

    @PostConstruct
    private  void configure(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        properties.put("mail.smtp.ssl.enable", "true");


        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        this.mailSender = mailSender;
    }

    public void sendRegistrationMessage(String registrationKey, String email){

        System.out.println(email);
        if (registrationKey == null || email == null) {
            throw new NullPointerException("Registration key or recipient email cannot be null");
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Registration link");
            helper.setText("http://localhost:8090/auth/register/"+registrationKey);

            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
            throw  new EmailServiceException("Unable to Send Email. Please, register again");
        }
    }

}
