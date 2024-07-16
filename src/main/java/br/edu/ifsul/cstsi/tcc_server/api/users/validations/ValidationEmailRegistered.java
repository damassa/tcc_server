package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.infra.exception.ValidationEmailRegisteredException;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationEmailRegistered implements ValidationUserRegister {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationEmailRegisteredException("Erro: E-mail já está cadastrado.");
        }
    }
}
