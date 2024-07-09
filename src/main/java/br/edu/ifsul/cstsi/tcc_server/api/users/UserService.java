package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.services.mail.EmailService;
import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository rep;
    @Autowired
    private TokenConfirmEmailRepository tokenConfirmEmailRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private List<ValidationRegister> validations;

    public User insert(User user) {
        Assert.isNull(user.getId(), "Não foi possível inserir o registro");

        validations.forEach(v -> v.validate(user));

        var savedUser = rep.save(user);
        var tokenConfirmEmail = new TokenConfirmEmail(savedUser);
        tokenConfirmEmailRepository.save(tokenConfirmEmail);

        emailService.sendEmail(
                user.getEmail(),
                "Solicitação de registro do usuário",
                "Olá, " + user.getName()
                +"\n\nAgora que você se registrou em World of Tokusatsu, com o e-mail " + user.getEmail()
                + " é necessário confirmá-lo, clicando no link a seguir:"
                + "\nhttp://localhost:8080/confirm-email?token=" + tokenConfirmEmail.getToken()
        );
        return savedUser;
    }

    public boolean confirmEmail(String TokenConfirmation) {
        var token = tokenConfirmEmailRepository.findByToken(TokenConfirmation);
        if(token != null) {
            var user = rep.findByEmail(token.getUser().getEmail());
            user.setConfirmed(true);
            rep.save(user);
            return true;
        }
        return false;
    }

    public List<User> getUsers() {
        return rep.findAll();
    }
}
