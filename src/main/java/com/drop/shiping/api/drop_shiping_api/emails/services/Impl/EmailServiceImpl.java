package com.drop.shiping.api.drop_shiping_api.emails.services.Impl;

import com.drop.shiping.api.drop_shiping_api.emails.dtos.EmailDTO;
import com.drop.shiping.api.drop_shiping_api.emails.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String emailFrom;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailDTO email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailFrom);
        message.setTo(emailFrom);
        message.setReplyTo(email.email());
//        message.setSubject("Formulario de contacto: " + email.);
        message.setText("""
            Nuevo mensaje de contacto
            
            Nombre: %s
            Email: %s
            
            Mensaje:
            %s
            """.formatted(
                email.name(),
                email.email(),
                email.message()
        ));

        mailSender.send(message);
    }
}
