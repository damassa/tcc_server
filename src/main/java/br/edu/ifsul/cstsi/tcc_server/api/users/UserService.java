package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.services.mail.EmailService;
import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationUserRegister;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository rep;
    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private TokenConfirmEmailRepository tokenConfirmEmailRepository;
    @Autowired
    private List<ValidationUserRegister> validations;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User insert(User user) {
        Assert.isNull(user.getId(), "Não foi possível inserir o registro");

        validations.forEach(v -> v.validate(user));

        var savedUser = rep.save(user);
        var tokenConfirmEmail = new TokenConfirmEmail(savedUser);
        tokenConfirmEmailRepository.save(tokenConfirmEmail);

        emailService.sendEmail(
                user.getEmail(),
                "Solicitação de cadastro no app Aulas TADS",
                "Olá, " + user.getName()
                        +"\n\nAgora que você se cadastrou World of Tokusatsu, com o email " + user.getEmail()
                        + " é necessário confirmá-lo, clicando no link a a seguir:"
                        + "\nhttps://tccserver-f1d0375900a5.herokuapp.com/confirm-email?token=" + tokenConfirmEmail.getToken());

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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(email);

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
