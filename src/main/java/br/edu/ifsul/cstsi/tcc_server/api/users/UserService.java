package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationUserRegister;
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
    private List<ValidationUserRegister> validations;

    public User insert(User user) {
        Assert.isNull(user.getId(), "Não foi possível inserir o registro");

        validations.forEach(v -> v.validate(user));

        var savedUser = rep.save(user);
        var tokenConfirmEmail = new TokenConfirmEmail(savedUser);
        tokenConfirmEmailRepository.save(tokenConfirmEmail);

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
