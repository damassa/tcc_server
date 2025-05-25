package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.services.mail.EmailService;
import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationUserRegister;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository rep;
    @Autowired
    private TokenConfirmEmailRepository tokenConfirmEmailRepository;
    @Autowired
    private List<ValidationUserRegister> validations;
    @Autowired
    private EmailService emailService;


    @Transactional
    public User insert(@Valid User user) {
        log.debug("Iniciando registro de usuário com o e-mail: {}", user.getEmail());
        Assert.isNull(user.getId(), "Não foi possível inserir o registro");

        validations.forEach(v -> v.validate(user));
        var savedUser = rep.save(user);
        var token = new TokenConfirmEmail(savedUser);
        tokenConfirmEmailRepository.save(token);

        emailService.sendEmail(
                user.getEmail(),
                "Solicitação de cadastro em World of Tokusatsu",
                "Olá, " + user.getName()
                + "\n\nAgora que você se cadastrou em World of Tokusatsu, com o e-mail " + user.getEmail()
                + " é necessário confirmar, clicando no link a seguir:"
                + "\n\nhttp://localhost:8080/confirm-email?token=" + token.getToken()
        );
        return savedUser;
    }

    @Transactional
    public boolean confirmEmail(String TokenConfirmation) {
        log.debug("Tentativa de confirmação de email com token: {}", TokenConfirmation);

        if (TokenConfirmation == null || TokenConfirmation.isBlank()) {
            log.warn("Token de confirmação vazio ou nulo");
            return false;
        }

        var token = tokenConfirmEmailRepository.findByToken(TokenConfirmation);
        if (token == null) {
            log.warn("Token não encontrado: {}", TokenConfirmation);
            return false;
        }

        //if(token != null) {
        //            var user = rep.findByEmail(token.getUser().getEmail());
        //            user.setConfirmed(true);
        //            rep.save(user);
        //            return true;
        //        }
        //        return false;

        //if (token.isExpired()) {
        //    return false;
        //}

        var user = rep.findByEmail(token.getUser().getEmail());
        if (user == null) {
            log.warn("Usuário não encontrado para o token: {}", TokenConfirmation);
            return false;
        }

        user.setConfirmed(true);
        rep.save(user);
        tokenConfirmEmailRepository.delete(token);

        log.info("E-mail confirmado com sucesso para: {}", user.getEmail());
        return true;
    }

    @Transactional
    public boolean requestPasswordReset(String email) {
        User user = rep.findByEmail(email);
        if (user == null) {
            return false;
        }

        // Gerar token e definir expiração
        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(24));
        rep.save(user);

        emailService.sendEmail(
                email,
                "Redefinição de Senha - World of Tokusatsu",
                "Olá, " + user.getName() +
                        "\n\nVocê solicitou a redefinição de sua senha. Para prosseguir, clique no link abaixo:" +
                        "\n\nhttp://localhost:3000/reset-password?token=" + user.getResetPasswordToken() +
                        "\n\nEste link é válido por 24 horas." +
                        "\n\nSe você não solicitou esta redefinição, ignore este e-mail."
        );
        return true;
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        User user = rep.findByResetPasswordToken(token);

        if (user == null ||
                user.getResetPasswordTokenExpiry() == null ||
                LocalDateTime.now().isAfter(user.getResetPasswordTokenExpiry())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        rep.save(user);

        return true;
    }


//    public boolean resendConfirmationEmail (String email) {
//        log.debug("Solicitação de reenvio de confirmação para: {}", email);
//
//        var user = rep.findByEmail(email);
//        if(user == null || user.isConfirmed()) {
//            log.warn("Não é possível reenviar e-mail: usuário não encontrado ou já confirmado.");
//            return false;
//        }
//
//        tokenConfirmEmailRepository.deleteByUser((user);
//
//        var newToken = new TokenConfirmEmail(user);
//        tokenConfirmEmailRepository.save(newToken);
//
//        emailService.sendEmail(user, newToken.getToken(), "E-mail de confirmação reenviado.");
//        log.info("E-mail de confirmação reenviado para: {}", user.getEmail());
//        return true;
//    }

    public List<User> getUsers() {
        return rep.findAll();
    }

    public User getUserById(Long id) {
        return rep.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return rep.findByEmail(email);
    }


    @Transactional
    public void updateUserById(UserUpdateDTO userEditDTO) {
        User user = getUserById(userEditDTO.id());
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        if (userEditDTO.name() != null && !userEditDTO.name().trim().isEmpty()) {
            user.setName(userEditDTO.name());
        }

        if (userEditDTO.email() != null && !userEditDTO.email().trim().isEmpty()) {
            // Verificar se o email já não está em uso por outro usuário
            User existingUser = getUserByEmail(userEditDTO.email());
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new ValidationException("E-mail já está em uso");
            }
            user.setEmail(userEditDTO.email());
        }

        rep.save(user);
    }


    public List<Serie> getFavoriteSeriesById(Long id) {
        var f = serieRepository.getFavoritesByUserID(id);
        return f;
    }
}