package br.edu.ifsul.cstsi.tcc_server.api.services.mail;

public interface EmailService {
    void sendEmail(String to, String subject, String message);
}
