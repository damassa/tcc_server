package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.services.mail.EmailService;
import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationUserRegister;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

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

    public User getUserByEmail(String email) {
        return rep.findByEmail(email);
    }

    @Transactional
    public void updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = rep.findByEmail(email);

        if(userUpdateDTO.newPassword() != null && !userUpdateDTO.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.newPassword()));
        }

        if(userUpdateDTO.name() != null && !userUpdateDTO.name().isBlank()) {
            user.setName(userUpdateDTO.name());
        }
        rep.save(user);
    }

    public List<Serie> getFavoriteSeriesById(Long id) {
        var f = serieRepository.getFavoritesByUserID(id);
        return f;
    }
}