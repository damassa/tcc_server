package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.auth.UsuarioDTO;
import br.edu.ifsul.cstsi.tcc_server.api.infra.exception.ValidationEmailNotConfirmedYetException;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationEmailNotConfirmedYet implements ValidationUserLogin {
    @Autowired
    private UserRepository rep;

    @Override
    public void validate(UsuarioDTO usuarioDTO) {
        if(!rep.findByEmail(usuarioDTO.email()).isConfirmed()) {
            throw new ValidationEmailNotConfirmedYetException("Erro: Este e-mail ainda não foi confirmado. Favor acessar caixa de e-mail e clicar no link para confirmar");
        }
    }
}
