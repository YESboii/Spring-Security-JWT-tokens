package com.example.JWTSecurity.Service;

import com.example.JWTSecurity.exceptionHandling.exceptions.EmailServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {


    private static JavaMailSenderImpl configure(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        properties.put("mail.smtp.ssl.enable", "true");


        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);
        mailSender.setUsername("foodieezproject123@gmail.com");
        mailSender.setPassword("dihclhwowhwkwnok");

        return mailSender;
    }

    public void sendRegistrationMessage(String registrationKey, String email){
        JavaMailSenderImpl mailSender = configure();
        System.out.println(email);
        int i=2;

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
