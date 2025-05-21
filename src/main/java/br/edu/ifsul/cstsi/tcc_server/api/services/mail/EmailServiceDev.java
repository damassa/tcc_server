package br.edu.ifsul.cstsi.tcc_server.api.services.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile({"dev", "test"})
public class EmailServiceDev implements EmailService {

    private String from = "suporte.tokusatsu@gmail.com";
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        emailSender.send(email);
        System.out.println("Email enviado com sucesso para " + to);
        System.out.println("Assunto: " + subject);
        System.out.println("Mensagem: " + message);
        System.out.println("---------------------------------------");
    }

}
