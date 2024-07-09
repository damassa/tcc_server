package br.edu.ifsul.cstsi.tcc_server.api.services.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev", "test"})
public class EmailServiceDev implements EmailService { //TODO: Rever sexta a questão de host, usuário e senha para mandar e-mail
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("felipelealdamasceno@gmail.com");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        emailSender.send(email);
    }
}