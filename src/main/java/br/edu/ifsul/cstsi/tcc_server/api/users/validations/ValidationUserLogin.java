package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.auth.UsuarioAuthDTO;

public interface ValidationUserLogin {
    void validate(UsuarioAuthDTO usuarioAuthDTO);
}
