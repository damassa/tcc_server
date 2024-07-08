package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.infra.exception.ValidationEmailNotConfirmedYetException;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationEmailNotConfirmedYet implements ValidationLoginUser {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(UsuarioDTO usuarioDTO) {
        if(!userRepository.findByLogin(usuarioDTO.email()).isConfirmed()) {
            throw new ValidationEmailNotConfirmedYetException("Erro: Esse e-mail ainda não foi confirmado. Favor acessar a caixa do e-mail e clicar no link de confirmação.");
        }
    }
}
